package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.performance.exception.PerformanceNotFound;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.ContentType.PERFORMANCE;
/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class PerformanceProvider implements CategoryProvider
{
    private final PerformanceRepository performanceRepository;

    @Override
    public boolean hasCategory(String category) {
        return  PERFORMANCE.name().equalsIgnoreCase(category);
    }

    /*
     * 공연은 공통 위치 가지므로 1개 반환
     */
    @Override
    public Object getMapMarkersByCategory() {
        MapInfoDto mapInfo = MapInfoDto.from(performanceRepository.findLocationFirstById());

        return MapFilteredByCategoryDto.from(List.of(mapInfo),PERFORMANCE.name());
    }

    @Override
    public ContentType ValidateContentExistsForScrap(Long categoryId) {
        if(!performanceRepository.existsById(categoryId))
            throw new PerformanceNotFound(ErrorCode.PERFORMANCE_NOT_FOUND);
        return PERFORMANCE;
    }
}
