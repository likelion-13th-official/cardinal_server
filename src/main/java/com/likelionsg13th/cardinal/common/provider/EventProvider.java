package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.event.exception.EventNotFound;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDetailDto;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.ContentType.EVENT;

/*
 * CASE 2
 * */
@Component
@RequiredArgsConstructor
public class EventProvider implements CategoryProvider{

    private final EventRepository eventRepository;


    @Override
    public boolean hasCategory(String category){
        return EVENT.name().equalsIgnoreCase(category.trim());
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
