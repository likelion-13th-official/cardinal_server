package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.repository.FoodTruckBoothRepository;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.BoothCategory.FOOD_TRUCK;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.PUB;

@Component
@RequiredArgsConstructor
public class FoodTruckProvider implements CategoryProvider{

    private final FoodTruckBoothRepository foodTruckBoothRepository;
    @Override
    public boolean hasCategory(String category) {
        return FOOD_TRUCK.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory() {
        List<Map> mapList= foodTruckBoothRepository.findLocationAll();

       List<MapInfoDto> mapInfoDtos =  mapList.stream()
               .map(MapInfoDto::from)
               .toList();

        return MapFilteredByCategoryDto.from(mapInfoDtos,FOOD_TRUCK.name());
    }
}
