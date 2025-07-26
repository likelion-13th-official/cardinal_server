package com.likelionsg13th.cardinal.pub.domain;

import com.likelionsg13th.cardinal.common.domain.Map;
import com.likelionsg13th.cardinal.common.domain.Menu;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false) //공지사항
    private String notice;

    @Column(nullable = false) //주점 소개
    private String description;
    //주점 고정요일로
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;


    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private String menuImageUrl;

    @Column(nullable = false)
    private String mapImageUrl;

    @Column(nullable = false)
    private String instagramUrl;

    //수정 one to one
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;

    //수정
//    @Column(nullable = false)
//    private List<Menu> menuList;


}
