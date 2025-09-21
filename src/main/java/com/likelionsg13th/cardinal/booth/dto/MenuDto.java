package com.likelionsg13th.cardinal.booth.dto;

import com.likelionsg13th.cardinal.booth.domain.Menu;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuDto {
    private Long id;
    private String name;
    private int price;

    public static MenuDto from(Menu menu) {
        return MenuDto.builder()
                .name(menu.getName())
                .price(menu.getPrice())

                .build();
    }
}
