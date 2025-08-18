package com.likelionsg13th.cardinal.booth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("마당사업")
@Getter
@SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YardBooth extends Booth{


}
