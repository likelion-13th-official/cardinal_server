package com.likelionsg13th.cardinal.users.dto.resonseDto;

import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter @AllArgsConstructor @Builder
public final class ScrapGoodsDetailDto implements ScrapDetail {
    Long price;

    public static ScrapGoodsDetailDto from (Goods goods){
        return  ScrapGoodsDetailDto.builder()
                .price(goods.getPrice())
                .build();
    }
}
