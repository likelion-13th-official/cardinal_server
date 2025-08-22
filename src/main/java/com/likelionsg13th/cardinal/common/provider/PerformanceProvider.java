package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.repository.FoodTruckBoothRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.ContentType.PERFORMANCE;

@Component
@RequiredArgsConstructor
public class PerformanceProvider implements CategoryProvider
{
    private final PerformanceRepository performanceRepository;

    @Override
    public boolean hasCategory(String category) {
        return  PERFORMANCE.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory() {
        MapInfoDto mapInfo = MapInfoDto.from(performanceRepository.findLocationFirstById());

        return MapFilteredByCategoryDto.from(List.of(mapInfo),PERFORMANCE.name());
    }
}
