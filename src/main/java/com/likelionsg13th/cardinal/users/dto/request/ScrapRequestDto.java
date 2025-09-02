package com.likelionsg13th.cardinal.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapRequestDto {
    @NotBlank
    String category;

    @NotNull(message = "categoryId는 필수 입력값입니다.")
    Long categoryId;
}

