package com.likelionsg13th.cardinal.goods.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.provider.GoodsProvider;
import com.likelionsg13th.cardinal.event.domain.EventDocument;
import com.likelionsg13th.cardinal.event.dto.EventSearchResponse;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.domain.GoodsDocument;
import com.likelionsg13th.cardinal.goods.dto.GoodsDetailResponse;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.exception.GoodsNotFound;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.beans.Transient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

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
        searchLogger.info("search performed",
                kv("query",query),
                kv("userId",userId));

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
    public PageDto<GoodsResponse> getGoodsList(int page, Long userId) {
        Pageable pageable=PageRequest.of(page-1,GOODS_PAGE_SIZE);
        Page<Goods> goodsPage = goodsRepository.findAll(pageable);


        Set<Long> scrappedGoodsIds = goodsProvider.getScrappedContentIds(goodsPage.stream().map(Goods::getId).toList()
                , userId);

        Page<GoodsResponse> goodsResponsePage = goodsPage.map(
                goods -> {
                    boolean isScrapped = scrappedGoodsIds.contains(goods.getId());
                    return GoodsResponse.from(goods, isScrapped);
                });

        //pageDTO에 담기
        return PageDto.from(goodsResponsePage);
    }

    /* GET /goods/{id} */
    public GoodsDetailResponse getGoods(Long id, Long userId) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(()-> new GoodsNotFound(ErrorCode.GOODS_NOT_FOUND));

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
                                .fields("name^3", "description", "type^2")
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
