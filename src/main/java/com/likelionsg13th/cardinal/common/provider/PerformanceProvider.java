package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;

import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.exception.PerformanceNotFound;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import com.likelionsg13th.cardinal.users.dto.response.ScrapCommonDto;
import com.likelionsg13th.cardinal.users.dto.response.ScrapTimeDetailDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.likelionsg13th.cardinal.common.enums.ContentType.PERFORMANCE;
import static com.likelionsg13th.cardinal.common.enums.PerformanceCategory.FILM;

/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class PerformanceProvider implements CategoryProvider,Scrappable {
    private final PerformanceRepository performanceRepository;
    private final ScrapRepository scrapRepository;
    @Override
    public boolean hasCategory(String category) {
        return  PERFORMANCE.name().equalsIgnoreCase(category);
    }

    /*
    * - 공연
    - 아티스트, 동아리 : X
    - 영화제 :  +시작시간,끝시간
    * */
    @Override
    public Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating) {
        Optional<Performance> performanceOptional = performanceRepository.findById(contentId);
        if(performanceOptional.isEmpty()) return Optional.empty();
        Performance performance = performanceOptional.get();

        if(performance.getCategory().equals(FILM)){
            return isFilteredByDay(day,performance.getOperatingDays())
                    && isFilteredByIsOperating(isOperating,performance.getOperatingInfo().isOperating())
                    ? Optional.of(ScrapCommonDto.of(performance, ScrapTimeDetailDto.from(performance)))
                    : Optional.empty();
        }

        return Optional.of(ScrapCommonDto.of(performance, null));
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

    @Override
    public boolean isScrappedByUser(Long userId ,Long contentId){
        return scrapRepository.existsByUser_IdAndContentIdAndContentType(userId,contentId,PERFORMANCE);
    }

}
