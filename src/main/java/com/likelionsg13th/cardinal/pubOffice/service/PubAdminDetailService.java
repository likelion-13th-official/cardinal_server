package com.likelionsg13th.cardinal.pubOffice.service;

import com.likelionsg13th.cardinal.pubOffice.domain.PubAdmin;
import com.likelionsg13th.cardinal.pubOffice.repository.PubAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@RequiredArgsConstructor
@Service
public class PubAdminDetailService implements UserDetailsService {

    private final PubAdminRepository pubAdminRepository;


    @Override
    public CustomUserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
        return  pubAdminRepository.findByAdminId(adminId)
                .map(this::createAdminDetail)
                .orElseThrow(() -> new UsernameNotFoundException(adminId));
    }

    private CustomUserDetails createAdminDetail(PubAdmin pubAdmin){

        return new CustomUserDetails(
                pubAdmin.getAdminId(),
                pubAdmin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")),
                pubAdmin.getBooth().getId()
        );
    }
}
