package com.likelionsg13th.cardinal.common.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.BoothDocument;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.dto.BoothSearchResponse;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.domain.UnifiedDocument;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.AutoCompleteDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SearchResultDto;
import com.likelionsg13th.cardinal.common.provider.BoothProvider;
import com.likelionsg13th.cardinal.common.provider.EventProvider;
import com.likelionsg13th.cardinal.common.provider.GoodsProvider;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.domain.EventDocument;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.dto.EventSearchResponse;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.event.service.EventService;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.domain.GoodsDocument;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.goods.service.GoodsService;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BoothRepository boothRepository;
    private final EventRepository eventRepository;
    private final GoodsRepository goodsRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final BoothProvider boothProvider;
    private final GoodsProvider goodsProvider;
    private final EventProvider eventProvider;
    private final BoothService boothService;
    private final EventService eventService;
    private final GoodsService goodsService;

    //search 로거
    private static final Logger searchLogger= LoggerFactory.getLogger("cardinal.search");

    private static final int SEARCH_PAGE_SIZE = 4;

    /* 전체 통합 검색 */
    @Transactional(readOnly = true)
    public List<SearchResultDto> searchAll(String query, Long userId) {
        searchLogger.info("search performed", kv("query", query), kv("userId", userId));

        Pageable pageable = PageRequest.of(0, SEARCH_PAGE_SIZE);

        SearchHits<BoothDocument> boothResults = boothService.boothQuery(query, pageable);
        SearchHits<EventDocument> eventResults = eventService.eventQuery(query, pageable);
        SearchHits<GoodsDocument> goodsResults = goodsService.goodsQuery(query, pageable);

        List<SearchResultDto> results = new ArrayList<>();
        // 부스 결과 처리
        List<BoothSearchResponse> boothResponses=boothService.getFinalResponse(boothResults,boothService.getScrapInfo(userId,boothResults));
        results.add(SearchResultDto.from("부스", (int) boothResults.getTotalHits(), boothResponses));

        // 이벤트 결과 처리
        List<EventSearchResponse> eventResponses = eventService.getFinalResponse(eventResults,eventService.getScrapInfo(userId,eventResults));
        results.add(SearchResultDto.from("이벤트", (int) eventResults.getTotalHits(), eventResponses));

        // 굿즈 결과 처리
        List<GoodsResponse> goodsResponses = goodsService.getFinalResponse(goodsResults,goodsService.getScrapInfo(userId,goodsResults));
        results.add(SearchResultDto.from("굿즈", (int) goodsResults.getTotalHits(), goodsResponses));

        // 전체 개수가 많은 순서대로 정렬
        results.sort(Comparator.comparingInt(SearchResultDto::getTotalCount).reversed());

        return results;

    }



    /* 자동완성 */
    public List<AutoCompleteDto> getSuggestion(String query) {
        SourceFilter sourceFilter = new FetchSourceFilter(
                true,
                new String[]{"boothId", "eventId", "goodsId", "name", "category", "type"},
                new String[]{});

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                // 1순위: 카테고리/타입 이름이 정확히 일치
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .fields("category.keyword^4", "type^4")
                                        )
                                )
                                // 2순위: 이름/메뉴명 접두사 일치
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .type(TextQueryType.BoolPrefix)
                                                .fields("name.autocomplete^3")
                                        )
                                )
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .type(TextQueryType.BoolPrefix)
                                                .fields("menu^2")
                                        )
                                )
                                // 3순위: 그 외
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .fields("name", "description", "type^2", "category^2")
                                                .fuzziness("AUTO")
                                        )
                                )
                        )
                )
                .withPageable(PageRequest.of(0, 10))
                .withSourceFilter(sourceFilter)
                .build();


        SearchHits<AutoCompleteSourceDto> searchHits = elasticsearchOperations.search(
                nativeQuery,
                AutoCompleteSourceDto.class,
                IndexCoordinates.of("booths", "events", "goods")
        );

        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(source -> new AutoCompleteDto(
                        source.getEntityId(),
                        source.getName(),
                        source.getFinalContentsType()
                ))
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class AutoCompleteSourceDto {

        private Long boothId;
        private Long eventId;
        private Long goodsId;
        private String name;
        private String category;
        private String type;

        public long getEntityId() {
            if (boothId != null) return boothId;
            if (eventId != null) return eventId;
            return goodsId;
        }

        // contentsType을 결정
        public String getFinalContentsType() {
            //부스의 경우
            if (category != null && !category.isBlank()) {
                return category;
            }
            // 이벤트, 굿즈의 경우
            return type;
        }
    }



}
