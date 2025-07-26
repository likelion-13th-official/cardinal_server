package com.likelionsg13th.cardinal.event.domain;


import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.common.domain.Map;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;


//    private List<DetailImage> images;
}
