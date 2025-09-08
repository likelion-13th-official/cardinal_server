package com.likelionsg13th.cardinal.pubOffice.service;

import com.likelionsg13th.cardinal.pubOffice.domain.PubAdmin;
import com.likelionsg13th.cardinal.pubOffice.repository.PubAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PubAdminDetails implements UserDetailsService {

    private final PubAdminRepository pubAdminRepository;


    @Override
    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
        return  pubAdminRepository.findByAdminId(adminId)
                .map(this::createAdminDetail)
                .orElseThrow(() -> new UsernameNotFoundException(adminId));
    }

    private UserDetails createAdminDetail(PubAdmin pubAdmin){
        return User.builder()
                .username(pubAdmin.getAdminId())
                .password(pubAdmin.getPassword())
                .roles("ADMIN")
                .build();
    }
}
