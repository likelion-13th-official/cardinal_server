package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SearchResultDto;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BoothRepository boothRepository;
    private final EventRepository eventRepository;
    private final GoodsRepository goodsRepository;
    private final BoothService boothService;

    private static final int RESULT_LIMIT = 4;

    /* 검색 초기 화면 */
    public List<SearchResultDto> searchAll(String query, Long userId){
       Pageable pageable= PageRequest.of(0,RESULT_LIMIT);


        //스크랩 여부 포함해서 검색
        Page<BoothResponse> boothsPage = boothRepository.findWithScrapStatus(query, userId, pageable);
        Page<EventResponse> eventsPage = eventRepository.findByNameContainingWithScrapStatus(query, userId, pageable);
        Page<GoodsResponse> goodsPage = goodsRepository.findByNameContainingWithScrapStatus(query, userId, pageable);

        List<SearchResultDto> results=new ArrayList<>();
        results.add(SearchResultDto.from("부스",boothsPage));
        results.add(SearchResultDto.from("이벤트",eventsPage));
        results.add(SearchResultDto.from("굿즈",goodsPage));
        
        results.sort(Comparator.comparingInt(SearchResultDto::getTotalCount).reversed());//내림차순

        return results;

    }


}
