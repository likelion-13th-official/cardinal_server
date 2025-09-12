package com.likelionsg13th.cardinal.event.service;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.provider.EventProvider;
import com.likelionsg13th.cardinal.common.service.UpdateIsOperating;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.dto.EventDetailResponse;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.dto.EventSimpleResponse;
import com.likelionsg13th.cardinal.event.exception.EventNotFound;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.performance.dto.PerformanceResponse;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final int PAGE_SIZE = 2;
    private final EventRepository eventRepository;
    private final EventProvider eventProvider;
    private final UpdateIsOperating updateIsOperating;

    /* 검색*/
    @Transactional(readOnly = true)
    public PageDto<EventResponse> searchEvents(String query, int page, Long userId) {
        Pageable pageable= PageRequest.of(page-1,PAGE_SIZE);
        Page<Event> eventsPage=eventRepository.findByNameContaining(query,pageable);



        Set<Long> scrappedEventsIds = eventProvider.getScrappedContentIds(eventsPage.stream().map(Event::getId).toList()
                , userId);

        Page<EventResponse> eventsResponsePage = eventsPage.map(
                event -> {
                    boolean isScrapped = scrappedEventsIds.contains(event.getId());
                    return EventResponse.from(event, isScrapped);
                });
        return PageDto.from(eventsResponsePage);

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
}
