package com.likelionsg13th.cardinal.booth.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.exception.BoothNotFoundException;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.booth.repository.specification.BoothSpecification;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.common.provider.BoothProvider;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;

@Service
@RequiredArgsConstructor
public class BoothService {
    private final BoothRepository boothRepository;
    private static final int PAGE_SIZE = 10;
    private static final int SEARCH_PAGE_SIZE = 2;
    private final BoothProvider boothProvider;
    private final ScrapRepository scrapRepository;

    @Transactional(readOnly = true)
    public PageDto<BoothResponse> getBoothList(Long userId, String categoryStr, Boolean isOperating, String dayStr, int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);

        Specification<Booth> spec=null;
        if(!"ALL".equalsIgnoreCase(categoryStr)){
            try {
                BoothCategory category = BoothCategory.valueOf(categoryStr.toUpperCase());
                spec = BoothSpecification.hasCategory(category);
            }catch(IllegalArgumentException e){
                throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
            }
        }
        if(isOperating!=null){
            Specification<Booth> operatingSpec = BoothSpecification.isOperating(isOperating);
            // spec이 null이면(카테고리가 ALL) 새로할당. null이 아니면 연결
            spec = (spec == null) ? operatingSpec : spec.and(operatingSpec);
        }
        if(dayStr!=null){
            DayOfWeek day = DayOfWeek.valueOf(dayStr.toUpperCase());

            Specification<Booth> daySpec = BoothSpecification.hasDay(day);
            spec = (spec == null) ? daySpec : spec.and(daySpec);
        }

        //필터링+페이지네이션 적용해서 DB 조회
        Page<Booth> boothsPage=boothRepository.findAll(spec, pageable);

        //스크랩 처리
        Set<Long> scrappedBoothIds;
        if (userId != null && boothsPage.hasContent()) {
            List<Long> boothIds = boothsPage.getContent().stream().map(Booth::getId).toList();
            scrappedBoothIds = boothProvider.getScrappedContentIds(boothIds, userId);
        } else {
            scrappedBoothIds = Collections.emptySet();
        }


        Page<BoothResponse> boothResponsePage=boothsPage.map(
                booth -> {
                    boolean isScrapped = scrappedBoothIds.contains(booth.getId());
                    return BoothResponse.from(booth, isScrapped);
                }
        );
        return PageDto.from(boothResponsePage);

    }



    //개별 상세 조회
    @Transactional(readOnly = true)
    public BoothDetailResponse getBoothDetail(Long userId,long id) {
        Booth booth=boothRepository.findById(id)
                .orElseThrow(()->new BoothNotFoundException(ErrorCode.BOOTH_NOT_FOUND));

        boolean isScrapped=false;
        if(userId!=null){
            isScrapped=scrapRepository.existsByUser_IdAndContentIdAndContentType(userId,id,BOOTH);
        }
        return BoothDetailResponse.of(booth,isScrapped);
    }



    // 검색
    @Transactional(readOnly = true)
    public PageDto<BoothResponse> searchBooths(Long userId,String query, int page) {
        Pageable pageable= PageRequest.of(page-1,SEARCH_PAGE_SIZE);

        Page<BoothResponse> boothResponsePage = boothRepository.findWithScrapStatus(
                query, userId, BOOTH, pageable
        );


        return PageDto.from(boothResponsePage);
    }
}
