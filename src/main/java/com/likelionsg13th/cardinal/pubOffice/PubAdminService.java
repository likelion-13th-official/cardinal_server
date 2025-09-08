package com.likelionsg13th.cardinal.pubOffice;

import com.likelionsg13th.cardinal.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PubAdminService {
    private final PubAdminRepository pubAdminRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    public PubAdminLoginResposne pubAdminLogin(PubAdminLogin loginDto){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getAdminId(),loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);

        PubAdmin pubAdmin = pubAdminRepository.findByAdminId(authentication.getName()).orElseThrow();
        if(!passwordEncoder.matches(loginDto.getPassword(),pubAdmin.getPassword())){
            throw new RuntimeException("password error");
        }

        return PubAdminLoginResposne.of(
                 pubAdmin.getId()
                ,authService.issueToken(pubAdmin.getAdminId(), pubAdmin.getBooth().getId()));

    }
}
