package com.likelionsg13th.cardinal.users.domain;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ActivityType activityType;

    @Column(nullable=false)
    private LocalDateTime earnedAt;

    @PrePersist
    public void prePersist() {
        this.earnedAt=LocalDateTime.now();
    }

}
