package com.likelionsg13th.cardinal.booth.domain.subtype;


import com.likelionsg13th.cardinal.booth.domain.Booth;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("제휴")
@Getter
@SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartnershipBooth extends Booth {

    private String logoImageUrl;
}
