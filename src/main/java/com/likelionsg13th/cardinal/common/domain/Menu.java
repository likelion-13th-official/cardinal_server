package com.likelionsg13th.cardinal.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    private String name;
    private int price;
}
