package com.likelionsg13th.cardinal.event.service;

import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.provider.EventProvider;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.domain.EventDocument;
import com.likelionsg13th.cardinal.event.dto.EventDetailResponse;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.dto.EventSimpleResponse;
import com.likelionsg13th.cardinal.event.exception.EventNotFound;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final int PAGE_SIZE = 2;
    private final EventRepository eventRepository;
    private final EventProvider eventProvider;
    private final ElasticsearchOperations elasticsearchOperations;
    private static final int SEARCH_PAGE_SIZE = 10;


    @Transactional(readOnly = true)
    public PageDto<EventResponse> searchEvents(String query, int page, Long userId) {
        Pageable pageable = PageRequest.of(page - 1, SEARCH_PAGE_SIZE);


        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(mm -> mm
                                .query(query)
                                .fields("name^3", "description^1", "type^2")
                                .fuzziness("AUTO")
                        )
                )
                .withPageable(pageable)
                .build();

        SearchHits<EventDocument> searchHits = elasticsearchOperations.search(nativeQuery, EventDocument.class, IndexCoordinates.of("events"));

        List<Long> eventIds = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(EventDocument::getEventId)
                .collect(Collectors.toList());

        if (eventIds.isEmpty()) {
            return PageDto.from(Page.empty());
        }

        // DB에서 상세 정보 조회
        Map<Long, Event> eventMap = eventRepository.findAllById(eventIds).stream()
                .collect(Collectors.toMap(Event::getId, event -> event));

        // 스크랩 정보 조회
        Set<Long> scrappedEventIds = (userId != null)
                ? eventProvider.getScrappedContentIds(eventIds, userId)
                : Collections.emptySet();

        // 최종 응답 (관련도 순서 유지)
        List<EventResponse> eventResponses = eventIds.stream()
                .map(eventMap::get)
                .map(event -> EventResponse.from(event, scrappedEventIds.contains(event.getId())))
                .collect(Collectors.toList());

        Page<EventResponse> eventResponsePage = new PageImpl<>(eventResponses, pageable, searchHits.getTotalHits());

        return PageDto.from(eventResponsePage);
    }




    public EventDetailResponse getEvent(Long id, Long userId ) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new EventNotFound(ErrorCode.EVENT_NOT_FOUND));

        boolean isScrapped = false;
        if (userId != null) {
            var scrapped = eventProvider.getScrappedContentIds(List.of(id), userId);
            isScrapped = scrapped.contains(id);
        }
        return EventDetailResponse.from(event, isScrapped);

    }

    public List<EventSimpleResponse> getEventList(DayOfWeek day){
        List<EventSimpleResponse> eventList= eventRepository.findAll().stream()
                .filter(e -> day==null ||
                        (e.getOperatingDays() !=null && e.getOperatingDays().contains(day)))
                .map(EventSimpleResponse::from)
                .toList();

        return eventList;
    }
}
