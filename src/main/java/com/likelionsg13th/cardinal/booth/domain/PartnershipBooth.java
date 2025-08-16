package com.likelionsg13th.cardinal.booth.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PartnershipBooth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartnershipBooth extends Booth{

    private String logoImageUrl;
}
