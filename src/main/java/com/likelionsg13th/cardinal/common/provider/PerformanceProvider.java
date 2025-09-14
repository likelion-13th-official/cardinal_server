package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;

import com.likelionsg13th.cardinal.common.service.UpdateIsOperating;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.exception.PerformanceNotFound;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.dto.response.ScrapCommonDto;
import com.likelionsg13th.cardinal.users.dto.response.ScrapTimeDetailDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;
import static com.likelionsg13th.cardinal.common.enums.ContentType.PERFORMANCE;
import static com.likelionsg13th.cardinal.common.enums.PerformanceCategory.*;

/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class PerformanceProvider implements CategoryProvider,Scrappable {

    private final PerformanceRepository performanceRepository;
    private final ScrapRepository scrapRepository;
    private final UpdateIsOperating updateIsOperating;

    @Override
    public boolean hasCategory(String category) {
        return  PERFORMANCE.name().equalsIgnoreCase(category);
    }

    /*
    - 공연
    - 아티스트, 동아리 : X :
    - 영화제 :  +시작시간,끝시간
    */
    @Override
    public Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating,Long scrapId) {
        Optional<Performance> performanceOptional = performanceRepository.findById(contentId);
        if(performanceOptional.isEmpty()) return Optional.empty();
        Performance performance = performanceOptional.get();

        //실시간 운영여부 계산
        boolean currentIsOperating = updateIsOperating.updateOperatingStatus(performance.getOperatingInfo(),performance.getOperatingDays());

        boolean isFiltered = isFilteredByDay(day, performance.getOperatingDays())
                && isFilteredByIsOperating(isOperating, currentIsOperating);

        if(isFiltered) {
            if(performance.getCategory().equals(FILM)) {return Optional.of(ScrapCommonDto.of(performance, ScrapTimeDetailDto.from(performance),scrapId));}
            else if(performance.getCategory().equals(ARTIST) || performance.getCategory().equals(CLUB)) return Optional.of(ScrapCommonDto.of(performance,null,scrapId));


        }
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
    @Override
    public void deleteScrapyByContentId(Long contentId, UserDto userDto) {
        scrapRepository.deleteByUser_IdAndContentIdAndContentType(userDto.getId(),contentId,PERFORMANCE);
    }


    public Set<Long> getScrappedContentIds(List<Long> contentIds , Long userId){
        return Scrappable.super.getScrappedContentIds(contentIds,userId,PERFORMANCE,scrapRepository);

    }

}
