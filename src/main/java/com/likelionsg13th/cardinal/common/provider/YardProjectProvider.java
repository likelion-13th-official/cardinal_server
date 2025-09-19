package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDetailDto;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryItemDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.BoothCategory.FOOD_TRUCK;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.YARD_PROJECT;
/*
 * CASE 2
 * */
@Component
@RequiredArgsConstructor
public class YardProjectProvider implements CategoryProvider{

    private final BoothRepository boothRepository;


    @Override
    public boolean hasCategory(String category){
        return YARD_PROJECT.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory() {
        List<Map> mapList= boothRepository.findLocationAllDistinctByCategory(YARD_PROJECT);

        List<MapInfoDto> mapInfoDtos =  mapList.stream()
                .map(MapInfoDto::from)
                .toList();

        return MapFilteredByCategoryDto.from(mapInfoDtos,YARD_PROJECT.name());

    }
}
