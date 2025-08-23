package com.likelionsg13th.cardinal.event.service;

import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.dto.EventDetailResponse;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.dto.EventSimpleResponse;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final int PAGE_SIZE = 2;
    private final EventRepository eventRepository;

    /* 검색*/
    @Transactional(readOnly = true)
    public PageDto<EventResponse> searchEvents(String query, int page) {
        Pageable pageable= PageRequest.of(page-1,PAGE_SIZE);
        Page<Event> eventsPage=eventRepository.findByNameContaining(query,pageable);

        Page<EventResponse> eventResponsePage=eventsPage.map(EventResponse::from);

        return PageDto.from(eventResponsePage);

    }

    public EventDetailResponse getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID의 events을 찾을 수 없습니다. ID: "+id));
        return EventDetailResponse.from(event);

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
