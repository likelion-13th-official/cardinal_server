package com.likelionsg13th.cardinal.goods.dto;

import com.likelionsg13th.cardinal.goods.domain.Goods;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class GoodsResponse {

    private Long id;

    private String name;
    private Long price;
    private String thumbnailUrl;

    private boolean isScrapped;


    public static GoodsResponse from(Goods goods) {
        return GoodsResponse.builder()
                .id(goods.getId())
                .name(goods.getName())
                .price(goods.getPrice())
                .thumbnailUrl(goods.getThumbnailUrl())
                //TODO: 북마크 확인 로직
                .isScrapped(false)
                .build();

    }
}
