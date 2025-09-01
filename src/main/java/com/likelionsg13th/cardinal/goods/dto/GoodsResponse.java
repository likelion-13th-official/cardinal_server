package com.likelionsg13th.cardinal.goods.dto;

import com.likelionsg13th.cardinal.goods.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder @AllArgsConstructor
public class GoodsResponse {

    private Long id;

    private String name;
    private Long price;
    private String thumbnailUrl;

    private boolean isScrapped;


    public GoodsResponse(Goods goods, boolean isScrapped) {
        this.id = goods.getId();
        this.name = goods.getName();
        this.price = goods.getPrice();
        this.thumbnailUrl = goods.getThumbnailUrl();
        this.isScrapped = isScrapped;
    }

    public static GoodsResponse from(Goods goods, boolean isScrapped) {
        return new GoodsResponse(goods, isScrapped);
    }
}
