package com.likelionsg13th.cardinal.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Menu {

    private String name;
    private int price;
}
