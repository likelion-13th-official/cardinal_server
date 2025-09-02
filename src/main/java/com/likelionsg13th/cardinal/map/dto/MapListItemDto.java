package com.likelionsg13th.cardinal.map.dto;

import com.likelionsg13th.cardinal.booth.domain.Booth;

import com.likelionsg13th.cardinal.booth.domain.Menu;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MapListItemDto {

     Long id;
     boolean bookMarked;
     List<String> operatingDays;
     LocalTime startTime;
     LocalTime endTime;
     String title;
     List<String> menus;
     String thumbnailUrl;


    public static MapListItemDto from(Booth booth, boolean bookMarked) {
        return new MapListItemDto(
                booth.getId(),
                bookMarked,
                booth.getOperatingDays().stream().map(DayOfWeek::toKorean).collect(Collectors.toList()),
                booth.getOperatingInfo().getStartTime(),
                booth.getOperatingInfo().getEndTime(),
                booth.getName(),
                booth.getMenus().stream().map(Menu::getName).collect(Collectors.toList()),
                booth.getThumbnailUrl()

        );

    }

}
