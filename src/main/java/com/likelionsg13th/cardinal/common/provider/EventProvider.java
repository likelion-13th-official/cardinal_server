package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.EventNotFound;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDetailDto;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryItemDto;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapCommonDto;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapTimeDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.likelionsg13th.cardinal.common.enums.ContentType.EVENT;
import static com.likelionsg13th.cardinal.common.enums.DayOfWeek.ALWAYS;

/*
 * CASE 2
 * */
@Component
@RequiredArgsConstructor
public class EventProvider implements CategoryProvider,Scrappable{

    private final EventRepository eventRepository;


    @Override
    public boolean hasCategory(String category){
        return EVENT.name().equalsIgnoreCase(category.trim());
    }

    @Override
    public Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating){

        Optional<Event> eventOptional = eventRepository.findById(contentId);
        if(eventOptional.isEmpty()) return Optional.empty();

        Event event  = eventOptional.get();

        return isFilteredByDay(day,event.getOperatingDays())
                && isFilteredByIsOperating(isOperating,event.getOperatingInfo().isOperating())
                ? Optional.of(ScrapCommonDto.of(event, ScrapTimeDetailDto.from(event)))
                : Optional.empty();
    }

    @Override
    public Object getMapMarkersByCategory() {

        List<MapFilteredByCategoryItemDto> items = eventRepository.findAll().stream().map(
                Booth ->
                        MapFilteredByCategoryItemDto.from(
                                Booth.getName(),
                                Booth.getId(),
                                Booth.getLocation())
        ).toList();


        return MapFilteredByCategoryDetailDto.of(EVENT.name(), items);
    }

    @Override
    public ContentType ValidateContentExistsForScrap(Long categoryId) {
        if(!eventRepository.existsById(categoryId))
            throw new EventNotFound(ErrorCode.EVENT_NOT_FOUND);
        return EVENT;
    }
}
