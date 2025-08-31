package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.exception.BoothNotFoundException;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.PerformanceNotFound;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapCommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;
import static com.likelionsg13th.cardinal.common.enums.ContentType.PERFORMANCE;
/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class PerformanceProvider implements CategoryProvider,Scrappable
{
    private final PerformanceRepository performanceRepository;

    @Override
    public boolean hasCategory(String category) {
        return  PERFORMANCE.name().equalsIgnoreCase(category);
    }

    @Override
    public Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating) {
        return Optional.empty();
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
