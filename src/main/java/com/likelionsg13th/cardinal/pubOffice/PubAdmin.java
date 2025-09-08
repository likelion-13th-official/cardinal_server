package com.likelionsg13th.cardinal.pubOffice;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PubAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String adminId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String department;

    @OneToOne
    @JoinColumn(name = "booth_id")
    private Booth booth;
}
