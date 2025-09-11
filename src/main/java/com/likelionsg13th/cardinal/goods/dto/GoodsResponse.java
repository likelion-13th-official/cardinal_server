package com.likelionsg13th.cardinal.goods.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.domain.GoodsDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder @AllArgsConstructor
public class GoodsResponse {

    private Long id;

    private String name;
    private Long price;
    private String thumbnailUrl;
    @JsonProperty("isScrapped")
    private boolean scrapped;


    public static GoodsResponse from(GoodsDocument doc, boolean isScrapped) {
        return GoodsResponse.builder()
                .id(doc.getGoodsId())
                .name(doc.getName())
                .price(doc.getPrice())
                .thumbnailUrl(doc.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();
    }

    public static GoodsResponse from(Goods goods, boolean isScrapped) {
        return GoodsResponse.builder()
                .id(goods.getId())
                .name(goods.getName())
                .price(goods.getPrice())
                .thumbnailUrl(goods.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();
    }
}
