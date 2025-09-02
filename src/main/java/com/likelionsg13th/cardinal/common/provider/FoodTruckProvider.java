package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.BoothCategory.FOOD_TRUCK;

/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class FoodTruckProvider implements CategoryProvider{

    private final BoothRepository foodTruckBoothRepository;
    @Override
    public boolean hasCategory(String category) {
        return FOOD_TRUCK.name().equalsIgnoreCase(category);
    }
    /*
     * 푸드트럭은 여러 위치 가지므로 리스트 반환
     */
    @Override
    public Object getMapMarkersByCategory() {
        List<Map> mapList= foodTruckBoothRepository.findLocationAllDistinctByCategory(FOOD_TRUCK);

       List<MapInfoDto> mapInfoDtos =  mapList.stream()
               .map(MapInfoDto::from)
               .toList();

        return MapFilteredByCategoryDto.from(mapInfoDtos,FOOD_TRUCK.name());
    }
}
