package com.likelionsg13th.cardinal.booth.dto.subtype;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.booth.domain.Menu;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter @SuperBuilder
public class PubBoothResponse extends BoothDetailResponse {

    private List<Menu> menus;
    private List<String> menuImageUrls;

    private String notice;
    private LocalDateTime noticeUpdatedAt;

    private String instagramUrl;
    private String tableLayoutUrl;
    private String deptHost;

    public static PubBoothResponse of(Booth booth,boolean isScrapped){
        PubBooth b=(PubBooth) booth;
        return PubBoothResponse.builder()
                .id(b.getId())
                .category(b.getCategory().toKorean())
                .name(b.getName())
                .location(b.getLocation().getPosition())
                .operatingInfo(b.getOperatingInfo())
                .description(b.getDescription())
                .thumbnailUrl(b.getThumbnailUrl())
                .operatingDays(
                        b.getOperatingDays().stream()
                                .map(day -> day.toKorean())
                                .collect(Collectors.toList())
                )
                .deptHost(b.getDeptHost())
                .menus(b.getMenus())
                .menuImageUrls(
                        b.getDetailImageList().stream()
                                .filter(Objects::nonNull)
                                .map(DetailImage::getImageUrl)
                                .collect(Collectors.toList())
                )

                .notice(b.getNotice())
                .noticeUpdatedAt(b.getNoticeUpdatedAt())
                .instagramUrl(b.getInstagramUrl())
                .tableLayoutUrl(b.getTableLayoutUrl())
                .scrapped(isScrapped)
                .build();

    }
}
