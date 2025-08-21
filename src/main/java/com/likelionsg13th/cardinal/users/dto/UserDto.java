package com.likelionsg13th.cardinal.users.dto;

import com.likelionsg13th.cardinal.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;

    public static UserDto of(Users user) {
        return UserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

}