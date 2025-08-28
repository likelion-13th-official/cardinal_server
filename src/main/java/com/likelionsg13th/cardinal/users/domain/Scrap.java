package com.likelionsg13th.cardinal.users.domain;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false)
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static Scrap toEntity(ContentType contentType,Long contentId,Users user){
        return Scrap.builder()
                .user(user)
                .contentId(contentId)
                .contentType(contentType)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
