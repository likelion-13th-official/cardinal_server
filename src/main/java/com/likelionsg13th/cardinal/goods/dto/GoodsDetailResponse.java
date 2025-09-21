package com.likelionsg13th.cardinal.goods.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter @Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class GoodsDetailResponse {

    private Long id;

    private String name;
    private Long price;
    private String thumbnailUrl;
    private String description;

    private List<String> detailImageList;
    private boolean scrapped;


    public static GoodsDetailResponse from(Goods goods, boolean isScrapped) {
        List<String> imageList = Optional.ofNullable(goods.getDetailImageList())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)          // 리스트 안의 null 요소 방지
                .map(DetailImage::getImageUrl)          // url 뽑기
                .filter(Objects::nonNull)          // url 자체가 null인 경우 방지
                .toList();
        return GoodsDetailResponse.builder()
                .id(goods.getId())
                .name(goods.getName())
                .price(goods.getPrice())
                .thumbnailUrl(goods.getThumbnailUrl())
                .description(goods.getDescription())
                .detailImageList(imageList)
                //TODO: 북마크 확인 로직
                .scrapped(isScrapped)
                .build();
    }
}
