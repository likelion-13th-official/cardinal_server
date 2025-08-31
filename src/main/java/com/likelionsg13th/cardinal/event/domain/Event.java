package com.likelionsg13th.cardinal.event.domain;


import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.common.domain.OperationAwareEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends OperationAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;

    @Column(nullable = false)
    private String description;

    private String applicationFormUrl; // 참가 신청 폼 경로

    @Column(nullable = false)
    private String thumbnailUrl;

    @CollectionTable(name="event_detail_images",joinColumns = @JoinColumn(name="event_id"))
    @ElementCollection
    @OrderColumn(name = "image_order")
    private List<DetailImage> detailImageList = new ArrayList<>();

    private long viewCount;
}