package com.likelionsg13th.cardinal.pubOffice.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

    private final Long pubId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long pubId) {
        super(username, password, authorities);
        this.pubId = pubId;
    }

}