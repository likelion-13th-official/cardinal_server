package com.likelionsg13th.cardinal.booth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("FoodTruckBooth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodTruckBooth extends Booth{


    private String menuImageUrl;
}
