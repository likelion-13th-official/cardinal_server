package com.likelionsg13th.cardinal.booth.domain;

import com.likelionsg13th.cardinal.common.domain.Map;
import com.likelionsg13th.cardinal.common.domain.Menu;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoothCategory category;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;

    @Embedded
    private OperatingInfo operatingInfo;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;

    @ElementCollection
    @CollectionTable(
            name = "menus",
            joinColumns = @JoinColumn(name = "booth_id")
    )
    private List<Menu> menus = new ArrayList<>();

    private Long viewCount;
}
