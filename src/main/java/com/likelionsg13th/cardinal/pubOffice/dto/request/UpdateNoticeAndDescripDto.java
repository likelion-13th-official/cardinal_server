package com.likelionsg13th.cardinal.pubOffice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateNoticeAndDescripDto {
    @NotBlank(message = "공지사항은 비워둘 수 없습니다.") // null, "", " " 모두 허용 안 함
    @Size(max = 200, message = "공지사항은 200자를 넘을 수 없습니다.")
    private String notice;
    @NotBlank(message = "소개글은 비워둘 수 없습니다.")
    private String description;
}
