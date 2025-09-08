package com.likelionsg13th.cardinal.common.service;

import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.domain.UnifiedDocument;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SearchResultDto;
import com.likelionsg13th.cardinal.common.provider.BoothProvider;
import com.likelionsg13th.cardinal.common.provider.EventProvider;
import com.likelionsg13th.cardinal.common.provider.GoodsProvider;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
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

    private static final int SEARCH_PAGE_SIZE = 4;

    @Transactional(readOnly = true)
    public List<SearchResultDto> searchAll(String query, Long userId) {
        //검색
        SearchHits<UnifiedDocument> searchHits = searchUnified(query);

        //검색 결과 타입별로 분류
        Map<String, List<SearchHit<UnifiedDocument>>> hitsByIndex = searchHits.getSearchHits().stream()
                .collect(Collectors.groupingBy(SearchHit::getIndex));

        // 타입별 응답 dto생성
        List<SearchResultDto> results = new ArrayList<>();

        results.add(createSearchResult("부스", hitsByIndex.getOrDefault("booths", Collections.emptyList()),
                ids -> boothRepository.findAllByIdIn(ids).stream().collect(Collectors.toMap(Booth::getId, Function.identity())),
                (ids, uId) -> boothProvider.getScrappedContentIds(ids, uId), BoothResponse::from, userId));

        results.add(createSearchResult("이벤트", hitsByIndex.getOrDefault("events", Collections.emptyList()),
                ids -> eventRepository.findAllById(ids).stream().collect(Collectors.toMap(Event::getId, Function.identity())),
                (ids, uId) -> eventProvider.getScrappedContentIds(ids, uId), EventResponse::from, userId));

        results.add(createSearchResult("굿즈", hitsByIndex.getOrDefault("goods", Collections.emptyList()),
                ids -> goodsRepository.findAllById(ids).stream().collect(Collectors.toMap(Goods::getId, Function.identity())),
                (ids, uId) -> goodsProvider.getScrappedContentIds(ids, uId), GoodsResponse::from, userId));

        // 결과 개수 순으로 내림차순 정렬
        results.sort(Comparator.comparingLong(SearchResultDto::getTotalCount).reversed());

        return results;
    }

    private SearchHits<UnifiedDocument> searchUnified(String query) {
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .fields("name^3", "description", "category^2", "type^2")
                                                .fuzziness("AUTO")
                                        )
                                )
                                .should(s -> s
                                        .nested(n -> n
                                                .path("menu")
                                                .query(nq -> nq
                                                        .match(m -> m
                                                                .field("menu.itemName")
                                                                .query(query)
                                                                .fuzziness("AUTO")
                                                        )
                                                )
                                                .ignoreUnmapped(true) //menu필드 없으면 무시
                                        )
                                )
                        )
                )
                .withPageable(PageRequest.of(0, SEARCH_PAGE_SIZE * 3))
                .build();

        return elasticsearchOperations.search(nativeQuery, UnifiedDocument.class,
                IndexCoordinates.of("booths", "events", "goods"));
    }

    private <T, R> SearchResultDto createSearchResult(
            String contentsName, List<SearchHit<UnifiedDocument>> hits,
            Function<List<Long>, Map<Long, T>> dbFetcher,
            BiFunction<List<Long>, Long, Set<Long>> scrapFetcher,
            BiFunction<T, Boolean, R> dtoConverter, Long userId) {

        List<Long> ids = hits.stream()
                .limit(SEARCH_PAGE_SIZE)
                .map(hit -> hit.getContent().getEntityId())
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return SearchResultDto.from(contentsName, 0, Collections.emptyList());
        }

        Map<Long, T> entityMap = dbFetcher.apply(ids);
        Set<Long> scrappedIds = (userId != null) ? scrapFetcher.apply(ids, userId) : Collections.emptySet();

        List<R> items = ids.stream()
                .map(entityMap::get)
                .filter(Objects::nonNull)
                .map(entity -> dtoConverter.apply(entity, scrappedIds.contains(getEntityId(entity))))
                .collect(Collectors.toList());

        return SearchResultDto.from(contentsName, hits.size(), items);
    }

    private Long getEntityId(Object entity) {
        if (entity instanceof Booth b) return b.getId();
        if (entity instanceof Event e) return e.getId();
        if (entity instanceof Goods g) return g.getId();
        throw new IllegalArgumentException("Unknown entity type");
    }


}
