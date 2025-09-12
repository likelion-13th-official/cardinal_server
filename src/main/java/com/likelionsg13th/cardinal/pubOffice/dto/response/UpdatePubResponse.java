package com.likelionsg13th.cardinal.pubOffice.dto.response;

import com.likelionsg13th.cardinal.booth.domain.Menu;
import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter @Builder
public class UpdatePubResponse {
    private Long pubId;
    private String name;
    private String category;

    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    private String description;
    private String location;

    private String thumbnailUrl;
    private List<Menu> menus;
    private List<String> menuImageUrls;

    private String notice;
    private LocalDateTime noticeUpdatedAt;

    private String instagramUrl;
    private String tableLayoutUrl;

    public static UpdatePubResponse of(PubBooth booth){
        PubBooth b=(PubBooth) booth;
        return UpdatePubResponse.builder()
                .pubId(b.getId())
                .category(b.getCategory().toKorean())
                .name(b.getName())
                .location(b.getLocation().getPosition())
                .operatingInfo(b.getOperatingInfo())
                .description(b.getDescription())
                .thumbnailUrl(b.getThumbnailUrl())
                .operatingDays(
                        b.getOperatingDays().stream()
                                .map(DayOfWeek::toKorean)
                                .collect(Collectors.toList())
                )
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
                .build();
    }

}
