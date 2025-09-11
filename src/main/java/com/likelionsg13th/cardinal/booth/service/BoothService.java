package com.likelionsg13th.cardinal.booth.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.BoothDocument;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.exception.BoothNotFoundException;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.booth.repository.specification.BoothSpecification;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.common.provider.BoothProvider;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;
//import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
public class BoothService {
    private final BoothRepository boothRepository;
    private static final int PAGE_SIZE = 10;
    private static final int SEARCH_PAGE_SIZE = 2;
    private final BoothProvider boothProvider;
    private final ScrapRepository scrapRepository;
    //ES
    private final ElasticsearchOperations elasticsearchOperations;
    //ES 로그
    private static final Logger searchLogger= LoggerFactory.getLogger("cardinal.search");



    @Transactional(readOnly = true)
    public PageDto<BoothResponse> getBoothList(Long userId, String categoryStr, Boolean isOperating, String dayStr, int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);

        Specification<Booth> spec=null;
        if(!"ALL".equalsIgnoreCase(categoryStr)){
            try {
                BoothCategory category = BoothCategory.valueOf(categoryStr.toUpperCase());
                spec = BoothSpecification.hasCategory(category);
            }catch(IllegalArgumentException e){
                throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
            }
        }
        if(isOperating!=null){
            Specification<Booth> operatingSpec = BoothSpecification.isOperating(isOperating);
            // spec이 null이면(카테고리가 ALL) 새로할당. null이 아니면 연결
            spec = (spec == null) ? operatingSpec : spec.and(operatingSpec);
        }
        if(dayStr!=null){
            DayOfWeek day = DayOfWeek.valueOf(dayStr.toUpperCase());

            Specification<Booth> daySpec = BoothSpecification.hasDay(day);
            spec = (spec == null) ? daySpec : spec.and(daySpec);
        }

        //필터링+페이지네이션 적용해서 DB 조회
        Page<Booth> boothsPage=boothRepository.findAll(spec, pageable);

        //스크랩 처리
        Set<Long> scrappedBoothIds;
        if (userId != null && boothsPage.hasContent()) {
            List<Long> boothIds = boothsPage.getContent().stream().map(Booth::getId).toList();
            scrappedBoothIds = boothProvider.getScrappedContentIds(boothIds, userId);
        } else {
            scrappedBoothIds = Collections.emptySet();
        }


        Page<BoothResponse> boothResponsePage=boothsPage.map(
                booth -> {
                    boolean isScrapped = scrappedBoothIds.contains(booth.getId());
                    return BoothResponse.from(booth, isScrapped);
                }
        );
        return PageDto.from(boothResponsePage);

    }



    //개별 상세 조회
    @Transactional(readOnly = true)
    public BoothDetailResponse getBoothDetail(Long userId,long id) {
        Booth booth=boothRepository.findById(id)
                .orElseThrow(()->new BoothNotFoundException(ErrorCode.BOOTH_NOT_FOUND));

        boolean isScrapped=false;
        if(userId!=null){
            isScrapped=scrapRepository.existsByUser_IdAndContentIdAndContentType(userId,id,BOOTH);
        }
        return BoothDetailResponse.of(booth,isScrapped);
    }



    /* ElasticSearch 이용 부스 검색*/
    @Transactional(readOnly = true)
    public PageDto<BoothResponse> searchBooths(Long userId, String query, int page) {
//        //로그 기록
//        searchLogger.info("search performed",
//                kv("query", query),
//                kv("userId",userId));

        //검색
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        SearchHits<BoothDocument> searchHits = boothQuery(query,pageable);
        //검색결과 아무것도 없을 경우
        if(searchHits.getTotalHits()==0)return PageDto.from(Page.empty());

        //부스 아이디 추출해서 스크랩 정보 가져오기
        Set<Long> scrappedBoothIds = getScrapInfo(userId, searchHits);

        //최종응답생성
        List<BoothResponse> boothResponses = getFinalResponse(searchHits,scrappedBoothIds);
        Page<BoothResponse> boothResponsePage = new PageImpl<>(boothResponses, pageable, searchHits.getTotalHits());

        return PageDto.from(boothResponsePage);
    }


    //검색 수행
    public SearchHits<BoothDocument> boothQuery(String query, Pageable pageable) {
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .fields("name^3", "description", "category^2", "menu")
                                                .fuzziness("AUTO")
                                        )
                                )
                        )
                )
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(nativeQuery, BoothDocument.class, IndexCoordinates.of("booths"));
    }


    //스크랩 정보
    public Set<Long> getScrapInfo(Long userId, SearchHits<BoothDocument> searchHits) {
        //부스 아이디 추출
        List<Long> boothIds = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(BoothDocument::getBoothId)
                .collect(Collectors.toList());

        Set<Long> scrappedBoothIds = (userId != null)
                ? boothProvider.getScrappedContentIds(boothIds, userId)
                : Collections.emptySet();
        return scrappedBoothIds;
    }

    //최종 응답 생성
    public List<BoothResponse> getFinalResponse  (SearchHits<BoothDocument> searchHits, Set<Long> scrappedBoothIds) {
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(boothDocument -> BoothResponse.from(boothDocument, scrappedBoothIds.contains(boothDocument.getBoothId())))
                .collect(Collectors.toList());
    }

}
