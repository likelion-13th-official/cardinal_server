package com.likelionsg13th.cardinal.booth.dto.subtype;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.PartnershipBooth;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.stream.Collectors;

@Getter @SuperBuilder
public class PartnershipBoothResponse extends BoothDetailResponse {

    private String logoImageUrl;

    public static PartnershipBoothResponse of(Booth booth){
        PartnershipBooth b=(PartnershipBooth) booth;
        return PartnershipBoothResponse.builder()
                .id(b.getId())
                .category(b.getCategory().toKorean())
                .name(b.getName())
                .location(b.getLocation().getPosition())
                .operatingInfo(b.getOperatingInfo())
                .thumbnailUrl(b.getThumbnailUrl())
                .description(b.getDescription())
                .operatingDays(
                        b.getOperatingDays().stream()
                                .map(day -> day.toKorean()) // DayOfWeek enum을 한글 문자열로 변환
                                .collect(Collectors.toList())
                )
                .logoImageUrl(b.getLogoImageUrl())
                .build();

    }
}
