package com.likelionsg13th.cardinal.users.dto.response;

import com.likelionsg13th.cardinal.goods.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @AllArgsConstructor @Builder
public final class ScrapGoodsDetailDto implements ScrapDetail {
    Long price;

    public static ScrapGoodsDetailDto from (Goods goods){
        return  ScrapGoodsDetailDto.builder()
                .price(goods.getPrice())
                .build();
    }
}
