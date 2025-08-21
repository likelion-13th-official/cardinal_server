package com.likelionsg13th.cardinal.booth.domain;

import com.likelionsg13th.cardinal.common.domain.*;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "booth_type")
public abstract class Booth extends OperationAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoothCategory category;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;


    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "menus",
            joinColumns = @JoinColumn(name = "booth_id")
    )
    private List<Menu> menus = new ArrayList<>();


    @CollectionTable(name="booth_detail_images",joinColumns = @JoinColumn(name="booth_id"))
    @ElementCollection
    @OrderColumn(name = "image_order")
    private List<DetailImage> detailImageList = new ArrayList<>();


    private long viewCount;
}
