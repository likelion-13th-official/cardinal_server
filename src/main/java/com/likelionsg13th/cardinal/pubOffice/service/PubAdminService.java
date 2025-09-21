package com.likelionsg13th.cardinal.pubOffice.service;

import com.likelionsg13th.cardinal.auth.service.AuthService;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;
import com.likelionsg13th.cardinal.pubOffice.dto.PubAdminDto;
import com.likelionsg13th.cardinal.pubOffice.dto.request.PubAdminLogin;
import com.likelionsg13th.cardinal.pubOffice.dto.request.UpdateNoticeAndDescripDto;
import com.likelionsg13th.cardinal.pubOffice.dto.response.PubAdminInfoResponse;
import com.likelionsg13th.cardinal.pubOffice.dto.response.PubAdminLoginResposne;
import com.likelionsg13th.cardinal.pubOffice.dto.response.UpdatePubResponse;
import com.likelionsg13th.cardinal.pubOffice.exception.UpdateNotAllowed;
import com.likelionsg13th.cardinal.pubOffice.repository.PubAdminRepository;
import com.likelionsg13th.cardinal.pubOffice.domain.PubAdmin;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PubAdminService {
    private final PubAdminRepository pubAdminRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final BoothRepository   boothRepository;



    public PubAdminLoginResposne pubAdminLogin(PubAdminLogin loginDto){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getAdminId(),loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);

        PubAdmin pubAdmin = pubAdminRepository.findByAdminId(authentication.getName()).orElseThrow();

        return PubAdminLoginResposne.of(
                 pubAdmin.getBooth().getId()
                ,authService.issueToken(pubAdmin.getAdminId(),pubAdmin.getBooth().getId()));

    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "boothDetail", key = "#pubId"),
            @CacheEvict(value = "boothList", allEntries = true)
    })
    public UpdatePubResponse updateNoticeAndDescription(Long pubId, UpdateNoticeAndDescripDto  updateNoticeAndDescripDto) {
        PubBooth pub = boothRepository.findPubBoothById(pubId).orElseThrow();
        pub.updateNoticeAndDescription(updateNoticeAndDescripDto);
        return UpdatePubResponse.of(pub);
    }

    public boolean canUpdate(CustomUserDetails principal,Long pubId ) {
        if(principal == null || principal.getUsername() == null ) return false;
        return principal.getPubId().equals(pubId);
    }


    public PubAdminInfoResponse checkAdminInfo(CustomUserDetails principal){
        if(principal == null || principal.getUsername() == null ) return null;
        PubAdmin pubAdmin = pubAdminRepository.findByAdminId(principal.getUsername()).orElseThrow();

        return PubAdminInfoResponse.builder()
                .pubId(pubAdmin.getBooth().getId())
                .department(pubAdmin.getDepartment())
                .build();

    }






}
