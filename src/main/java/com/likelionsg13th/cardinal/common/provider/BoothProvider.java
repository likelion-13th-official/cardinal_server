package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.exception.BoothNotFoundException;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapCommonDto;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapTimeDetailDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;
import static com.likelionsg13th.cardinal.common.enums.DayOfWeek.ALWAYS;

@Component
@RequiredArgsConstructor
public class BoothProvider implements CategoryProvider,Scrappable{
    private final BoothRepository boothRepository;


    @Override
    public boolean hasCategory(String category){
        return BOOTH.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory() {
        return null;
    }

    @Override
    public ContentType ValidateContentExistsForScrap(Long categoryId) {
        if(!boothRepository.existsById(categoryId))
            throw new BoothNotFoundException(ErrorCode.BOOTH_NOT_FOUND);
        return BOOTH;
    }



    @Override
    public Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating){

       Optional<Booth> boothOptional = boothRepository.findById(contentId);
       if(boothOptional.isEmpty()) return Optional.empty();
       Booth booth  = boothOptional.get();
       boolean matches = true;
       /*day 필터링 시 : 상시도 아니고, 필터 요일에 해당하지 않으면 -> 조건 부적합  */

       if(day != null
               && (!booth.getOperatingDays().contains(DayOfWeek.valueOf(day.toUpperCase()))
                && !booth.getOperatingDays().contains(ALWAYS) ) ) matches = false;

       if(isOperating!=null && booth.getOperatingInfo().isOperating() != isOperating ) matches = false;

       return matches ? Optional.of(ScrapCommonDto.of(booth, ScrapTimeDetailDto.from(booth))) : Optional.empty();
    }



}
