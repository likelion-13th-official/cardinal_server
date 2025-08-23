package com.likelionsg13th.cardinal.goods.service;

import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.dto.GoodsDetailResponse;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {

    private static final int PAGE_SIZE = 2;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int GOODS_PAGE_SIZE = 6;
    private final GoodsRepository goodsRepository;

    /* 검색 */
    @Transactional(readOnly = true)
    public PageDto<GoodsResponse> searchGoods(String query, int page) {
        Pageable pageable= PageRequest.of(page-1,PAGE_SIZE);
        Page<Goods> goodsPage=goodsRepository.findByNameContaining(query,pageable);

        //goods->goodsResponse DTO 변환
        Page<GoodsResponse> goodsResponsePage=goodsPage.map(GoodsResponse::from);

        //pageDTO에 담기
        return PageDto.from(goodsResponsePage);
    }

    public PageDto<GoodsResponse> getGoodsList(int page){
        Pageable pageable=PageRequest.of(page-1,GOODS_PAGE_SIZE);
        Page<Goods> goodsPage = goodsRepository.findAll(pageable);
        Page<GoodsResponse> goodsResponsePage=goodsPage.map(GoodsResponse::from);

        //pageDTO에 담기
        return PageDto.from(goodsResponsePage);
    }

    public GoodsDetailResponse getGoods(Long id){
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID의 goods을 찾을 수 없습니다. ID: "+id));
        return GoodsDetailResponse.from(goods);
    }

}
