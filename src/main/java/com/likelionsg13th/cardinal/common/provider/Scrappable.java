package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.dto.response.ScrapCommonDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;
import static com.likelionsg13th.cardinal.common.enums.DayOfWeek.ALWAYS;

/* BOOTH,EVENT,GOODS,PERFORMANCE*/
public interface Scrappable  {
    boolean hasCategory(String category);

    /* BOOTH,EVENT,PERFORMANCE*/
     default Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating,Long scrapId){
         return Optional.empty();
     }

    /*GOODS */
    default Optional<ScrapCommonDto> getScrapCommonDto(Long contentId,Long scrapId){
        return Optional.empty();
    }

    /* 요일별 필터링 공통 조건문 : DAY 파라미터 있으며 + (상시도 아니고, 해당 필터링요일과 일치하지 않을 시 FALSE)  */
    default boolean isFilteredByDay(String filterDay, List<DayOfWeek> operatingDays) {

        if(filterDay != null && !filterDay.isEmpty()
                && (!operatingDays.contains(DayOfWeek.valueOf(filterDay.toUpperCase()))
                && !operatingDays.contains(ALWAYS))) {
            return false;
        }
        else return true;
    }

    /* 운영 여부 필터링 공통 조건문 : ISOPERATING 파라미터가 있으며 , 일치 하지 않을 시 FALSE */
     default boolean isFilteredByIsOperating(Boolean filterIsOperating, boolean isOperating) {
         return filterIsOperating == null || isOperating == filterIsOperating;
    }

    /*delete scrap */
    void deleteScrapyByContentId(Long contentId,UserDto userDto);


    /*
     * 유저와 부스 리스트를 비교하여 스크랩 한 부스를 찾습니다.
     * @param boothList 스크랩 여부 확인할 부스 리스트
     * @param user 로그인한 사용자 정보 null 인 경우 빈 set 반환
     */
    default Set<Long> getScrappedContentIds(List<Long> contentIds , Long userId, ContentType contentType, ScrapRepository scrapRepository){
         if(userId == null ) return Collections.emptySet();

         return scrapRepository.findAllByUser_IdAndContentIdInAndContentType(userId,contentIds,contentType)
                 .stream()
                 .map(Scrap::getContentId)
                 .collect(Collectors.toSet());
     }
}
