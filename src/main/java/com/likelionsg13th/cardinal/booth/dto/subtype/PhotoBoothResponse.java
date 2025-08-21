package com.likelionsg13th.cardinal.booth.dto.subtype;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.PhotoBooth;

import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.common.domain.DetailImage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter @SuperBuilder
public class PhotoBoothResponse extends BoothDetailResponse {

    private List<String> detailImageUrls;

    public static PhotoBoothResponse of(Booth booth){
        PhotoBooth b=(PhotoBooth) booth;
        return PhotoBoothResponse.builder()
                .id(b.getId())
                .category(b.getCategory().toKorean())
                .name(b.getName())
                .location(b.getLocation().getPosition())
                .operatingInfo(b.getOperatingInfo())
                .thumbnailUrl(b.getThumbnailUrl())
                .description(b.getDescription())
                .operatingDays(
                        b.getOperatingDays().stream()
                                .map(day -> day.toKorean())
                                .collect(Collectors.toList())
                )
                .detailImageUrls(
                        b.getDetailImageList().stream()
                                .filter(Objects::nonNull)
                                .map(DetailImage::getImageUrl)
                                .collect(Collectors.toList())
                )

                .build();

    }

}
