package com.likelionsg13th.cardinal.performance.domain;


import com.likelionsg13th.cardinal.common.domain.OperationAwareEntity;
import com.likelionsg13th.cardinal.map.domain.Map;

import com.likelionsg13th.cardinal.common.enums.PerformanceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance extends OperationAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PerformanceCategory category;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;

    @Column(nullable = false)
    private String ThumbnailUrl;

    private long viewCount;
}
