package com.likelionsg13th.cardinal.users.domain;

import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Entity
public class Stamp {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    private LocalDateTime timestamp;

}
