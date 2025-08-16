package com.likelionsg13th.cardinal.booth.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PhotoBooth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoBooth  extends Booth{


}
