package com.likelionsg13th.cardinal.common.service;

import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionsg13th.cardinal.booth.domain.BoothDocument;
import com.likelionsg13th.cardinal.booth.dto.BoothSearchResponse;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SimpleSearchDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SearchResultDto;
import com.likelionsg13th.cardinal.common.provider.BoothProvider;
import com.likelionsg13th.cardinal.common.provider.EventProvider;
import com.likelionsg13th.cardinal.common.provider.GoodsProvider;
import com.likelionsg13th.cardinal.event.domain.EventDocument;
import com.likelionsg13th.cardinal.event.dto.EventSearchResponse;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.event.service.EventService;
import com.likelionsg13th.cardinal.goods.domain.GoodsDocument;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.goods.service.GoodsService;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


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

        Pageable pageable = PageRequest.of(0, SEARCH_PAGE_SIZE);

        SearchHits<BoothDocument> boothResults = boothService.boothQuery(query, pageable);
        SearchHits<EventDocument> eventResults = eventService.eventQuery(query, pageable);
        SearchHits<GoodsDocument> goodsResults = goodsService.goodsQuery(query, pageable);

        //인기검색어 관련
        findTopHit(boothResults, eventResults, goodsResults)
                .ifPresent(this::logSearch);

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
    public List<SimpleSearchDto> getSuggestion(String query) {
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
                .map(source -> new SimpleSearchDto(
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

//    //로깅
    private static final String SEARCH_KEY="popular_searches";
    private final RedisTemplate<String, String> redisTemplate;
    private volatile List<PopularSearchData> popularQueriesCache = Collections.emptyList();

    private Optional<SearchHit<?>> findTopHit(SearchHits<?>... searchHits) {
        List<SearchHit<?>> allHits = new ArrayList<>();
        for (SearchHits<?> hits : searchHits) {
            if (hits != null && hits.hasSearchHits()) {
                allHits.addAll(hits.getSearchHits());
            }
        }
        if (allHits.isEmpty()) {
            return Optional.empty();
        }
        SearchHit<?> topHit = allHits.get(0);
        for (int i = 1; i < allHits.size(); i++) {
            SearchHit<?> currentHit = allHits.get(i);
            if (Float.compare(currentHit.getScore(), topHit.getScore()) > 0) {
                topHit = currentHit;
            }
        }
        return Optional.of(topHit);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PopularSearchData {
        private Long id;
        private String name;
        private String type;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Async
    public void logSearch(SearchHit<?> topHit) {
        if (topHit == null || topHit.getContent() == null) {
            searchLogger.warn("logSearch called with null or empty SearchHit.");
            return;
        }

        Object content = topHit.getContent();
        PopularSearchData data = null;

        if (content instanceof BoothDocument) {
            BoothDocument doc = (BoothDocument) content;
            data = new PopularSearchData(doc.getBoothId(), doc.getName(), doc.getCategory());
        } else if (content instanceof EventDocument) {
            EventDocument doc = (EventDocument) content;
            data = new PopularSearchData(doc.getEventId(), doc.getName(), doc.getType());
        } else if (content instanceof GoodsDocument) {
            GoodsDocument doc = (GoodsDocument) content;
            data = new PopularSearchData(doc.getGoodsId(), doc.getName(), doc.getType());
        }

        //JSON으로 변환하여 Redis에 저장
        if (data != null) {
            try {
                String value = objectMapper.writeValueAsString(data);
                redisTemplate.opsForZSet().incrementScore(SEARCH_KEY, value, 1);
            } catch (JsonProcessingException e) {
                searchLogger.error("Error serializing popular search data to JSON", e);
            }
        }
    }

    @Scheduled(fixedRate = 30000)
    public void updatePopularQueries(){
        Set<String> top5Json = redisTemplate.opsForZSet().reverseRange(SEARCH_KEY, 0, 4);
        if (top5Json != null) {
            List<PopularSearchData> newCache = top5Json.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, PopularSearchData.class);
                        } catch (JsonProcessingException e) {
                            searchLogger.error("Error deserializing popular search data from Redis", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            this.popularQueriesCache = List.copyOf(newCache);
        }
    }

    @PostConstruct
    public void init(){
        updatePopularQueries();
    }

    public List<SimpleSearchDto> getPopularQueries() {
        return this.popularQueriesCache.stream()
                .map(data -> new SimpleSearchDto(
                        data.getId(),
                        data.getName(),
                        data.getType()
                ))
                .collect(Collectors.toList());
    }

}
