package com.likelionsg13th.cardinal.map.dto;

import com.likelionsg13th.cardinal.goods.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class MapSearchPriceDetailDto implements MapSearchDetail {
    private Long price;

    public static MapSearchPriceDetailDto from(Goods goods) {
        return MapSearchPriceDetailDto.builder()
                .price(goods.getPrice())
                .build();
    }
}
