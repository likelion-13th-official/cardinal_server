package com.likelionsg13th.cardinal.common.dto.resonseDto.map;

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
public class MapListDetailDto {

     Long id;
     boolean bookMarked;
     List<DayOfWeek> operatingDays;
     LocalTime startTime;
     LocalTime endTime;
     String title;
     List<String> menus;
     String thumbnailUrl;


    public static MapListDetailDto from(Booth booth, boolean bookMarked) {
        return new MapListDetailDto(
                booth.getId(),
                bookMarked,
                booth.getOperatingDays(),
                booth.getOperatingInfo().getStartTime(),
                booth.getOperatingInfo().getEndTime(),
                booth.getName(),
                booth.getMenus().stream().map(Menu::getName).collect(Collectors.toList()),
                booth.getThumbnailUrl()

        );

    }

}
