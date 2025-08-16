package com.likelionsg13th.cardinal.event.domain;


import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.common.domain.Map;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private OperatingInfo operatingInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;

    @CollectionTable(name="event_detail_images",joinColumns = @JoinColumn(name="event_id"))
    @ElementCollection
    @OrderColumn(name = "image_order")
    private List<DetailImage> detailImageList = new ArrayList<>();

    private Long viewCount;
}
