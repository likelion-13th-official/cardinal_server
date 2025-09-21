package com.likelionsg13th.cardinal.goods.service;

import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.provider.GoodsProvider;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.domain.GoodsDocument;
import com.likelionsg13th.cardinal.goods.dto.GoodsDetailResponse;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.exception.GoodsNotFound;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
public class GoodsService {


    private static final int GOODS_PAGE_SIZE = 6;
    private final GoodsRepository goodsRepository;
    private final GoodsProvider goodsProvider;
    private final ElasticsearchOperations elasticsearchOperations;
    private static final int SEARCH_PAGE_SIZE = 10;
    private static final Logger searchLogger= LoggerFactory.getLogger("cardinal.search");


    /* Elastic Search 검색 */
    @Transactional(readOnly = true)
    public PageDto<GoodsResponse> searchGoods(String query, int page, Long userId) {
//        searchLogger.info("search performed",
//                kv("query",query),
//                kv("userId",userId));

        //검색
        Pageable pageable = PageRequest.of(page - 1, SEARCH_PAGE_SIZE);
        SearchHits<GoodsDocument> searchHits = goodsQuery(query,pageable);
        if(searchHits.getTotalHits()==0)return PageDto.from(Page.empty());

        //스크랩정보
        Set<Long> scrappedGoodsIds=getScrapInfo(userId,searchHits);

        // 최종응답
        List<GoodsResponse> goodsResponses = getFinalResponse(searchHits,scrappedGoodsIds);
        Page<GoodsResponse> goodsResponsePage = new PageImpl<>(goodsResponses, pageable, searchHits.getTotalHits());

        return PageDto.from(goodsResponsePage);
    }

    /* GET /goods */
    @Cacheable(value = "goodsList", key = "'page:' + (#page ?: 'all') + ':userId:' + #userId")
    @Transactional(readOnly = true)
    public PageDto<GoodsResponse> getGoodsList(Integer page, Long userId) {
        // 전체 조회 (페이징 없이-프론트 요청사항)
        if (page == null || page <= 0) {
            List<Goods> goodsList = goodsRepository.findAll();
            
            //스크랩 여부 
            Set<Long> scrappedGoodsIds = goodsProvider.getScrappedContentIds(
                    goodsList.stream().map(Goods::getId).toList(),
                    userId
            );
            
            //ResponseDTO변환
            List<GoodsResponse> goodsResponses = goodsList.stream()
                    .map(goods -> {
                        boolean isScrapped = scrappedGoodsIds.contains(goods.getId());
                        return GoodsResponse.from(goods, isScrapped);
                    })
                    .toList();

            return PageDto.of(goodsResponses);
        }

        // 페이지 조회
        Pageable pageable = PageRequest.of(page - 1, GOODS_PAGE_SIZE);
        Page<Goods> goodsPage = goodsRepository.findAll(pageable);
        //스크랩여부
        Set<Long> scrappedGoodsIds = goodsProvider.getScrappedContentIds(
                goodsPage.stream().map(Goods::getId).toList(),
                userId
        );
        //DTO변환
        Page<GoodsResponse> goodsResponsePage = goodsPage.map(goods -> {
            boolean isScrapped = scrappedGoodsIds.contains(goods.getId());
            return GoodsResponse.from(goods, isScrapped);
        });

        return PageDto.from(goodsResponsePage);
    }


    /* GET /goods/{id} */
    @Transactional(readOnly = true)
    public GoodsDetailResponse getGoods(Long id, Long userId) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(()-> new GoodsNotFound(ErrorCode.GOODS_NOT_FOUND));

        //스크랩여부 
        boolean isScrapped = false;
        if (userId != null) {
            var scrapped = goodsProvider.getScrappedContentIds(List.of(id), userId);
            isScrapped = scrapped.contains(id);
        }
        return GoodsDetailResponse.from(goods, isScrapped);
    }

    //쿼리
    public SearchHits<GoodsDocument> goodsQuery(String query, Pageable pageable) {
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(mm -> mm
                                .query(query)
                                .fields("name^4", "description^0.5", "type^3")
                                .fuzziness("AUTO")
                        )
                )
                .withPageable(pageable)
                .build();
        return elasticsearchOperations.search(nativeQuery, GoodsDocument.class, IndexCoordinates.of("goods"));
    }

    //스크랩 정보
    public Set<Long> getScrapInfo(Long userId, SearchHits<GoodsDocument> searchHits) {
        //굿즈 아이디 추출
        List<Long> goodsIds = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(GoodsDocument::getGoodsId)
                .collect(Collectors.toList());

        Set<Long> scrappedGoodsIds = (userId != null)
                ? goodsProvider.getScrappedContentIds(goodsIds, userId)
                : Collections.emptySet();
        return scrappedGoodsIds;
    }

    //최종 응답 생성
    public List<GoodsResponse> getFinalResponse  (SearchHits<GoodsDocument> searchHits, Set<Long> scrappedGoodsIds) {
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(goodsDocument -> GoodsResponse.from(goodsDocument, scrappedGoodsIds.contains(goodsDocument.getGoodsId())))
                .collect(Collectors.toList());
    }
}
