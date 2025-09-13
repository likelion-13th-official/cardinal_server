package com.likelionsg13th.cardinal.event.service;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.provider.EventProvider;
import com.likelionsg13th.cardinal.common.service.UpdateIsOperating;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.domain.EventDocument;
import com.likelionsg13th.cardinal.event.dto.EventDetailResponse;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.dto.EventSimpleResponse;
import com.likelionsg13th.cardinal.event.exception.EventNotFound;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.performance.dto.PerformanceResponse;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final int PAGE_SIZE = 2;
    private final EventRepository eventRepository;
    private final EventProvider eventProvider;
    private final UpdateIsOperating updateIsOperating;
    private final ElasticsearchOperations elasticsearchOperations;
    private static final int SEARCH_PAGE_SIZE = 10;

    /* 검색*/
    @Transactional(readOnly = true)
    public PageDto<EventResponse> searchEvents(String query, int page, Long userId) {
        Pageable pageable = PageRequest.of(page - 1, SEARCH_PAGE_SIZE);
        SearchHits<EventDocument> searchHits = eventQuery(query,pageable);
        if(searchHits.getTotalHits()==0)return PageDto.from(Page.empty());
        //스크랩정보
        Set<Long> scrappedEventIds=getScrapInfo(userId,searchHits);

        List<EventResponse> eventResponses = getFinalResponse(searchHits,scrappedEventIds);
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

        OperatingInfo src = event.getOperatingInfo(); // 엔티티의 OI (절대 변경 X)
        OperatingInfo viewOi = null;
        if (src != null) {
            boolean currentIsOperating =
                    updateIsOperating.updateOperatingStatus(src, event.getOperatingDays());

            // ★ 복제본 생성 (도메인 수정/엔티티 변경 없음)
            viewOi = new OperatingInfo();
            viewOi.setStartTime(src.getStartTime());
            viewOi.setEndTime(src.getEndTime());
            viewOi.setOperating(currentIsOperating); // 계산값만 세팅
        }

        return EventDetailResponse.from(event, isScrapped, viewOi);

    }

    public List<EventSimpleResponse> getEventList(DayOfWeek day){
        List<EventSimpleResponse> eventList= eventRepository.findAll().stream()
                .filter(e -> day==null ||
                        (e.getOperatingDays() !=null && e.getOperatingDays().contains(day)))
                .map(p -> {
                    OperatingInfo src = p.getOperatingInfo(); // 엔티티의 OI (절대 변경 X)
                    OperatingInfo viewOi = null;

                    if (src != null) {
                        boolean currentIsOperating =
                                updateIsOperating.updateOperatingStatus(src, p.getOperatingDays());

                        // ★ 복제본 생성 (도메인 수정/엔티티 변경 없음)
                        viewOi = new OperatingInfo();
                        viewOi.setStartTime(src.getStartTime());
                        viewOi.setEndTime(src.getEndTime());
                        viewOi.setOperating(currentIsOperating); // 계산값만 세팅
                    }
                    return EventSimpleResponse.from(p, viewOi);
                })
                .toList();

        return eventList;
    }

    //쿼리수행
    public SearchHits<EventDocument> eventQuery(String query, Pageable pageable){
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
        return elasticsearchOperations.search(nativeQuery, EventDocument.class, IndexCoordinates.of("events"));
    }
    //스크랩 정보
    public Set<Long> getScrapInfo(Long userId, SearchHits<EventDocument> searchHits) {
        //이벤트 아이디 추출
        List<Long> eventIds = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(EventDocument::getEventId)
                .collect(Collectors.toList());

        Set<Long> scrappedEventIds = (userId != null)
                ? eventProvider.getScrappedContentIds(eventIds, userId)
                : Collections.emptySet();
        return scrappedEventIds;
    }

    //최종 응답 생성
    public List<EventResponse> getFinalResponse  (SearchHits<EventDocument> searchHits, Set<Long> scrappedEventIds) {
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(eventDocument -> EventResponse.from(eventDocument, scrappedEventIds.contains(eventDocument.getEventId())))
                .collect(Collectors.toList());
    }


}
