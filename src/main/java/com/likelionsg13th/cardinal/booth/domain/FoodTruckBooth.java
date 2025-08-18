package com.likelionsg13th.cardinal.booth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("푸드트럭")
@Getter
@SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodTruckBooth extends Booth{

    private String menuImageUrl;
}
