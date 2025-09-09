package com.likelionsg13th.cardinal.pubOffice.service;

import com.likelionsg13th.cardinal.auth.service.AuthService;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.pubOffice.dto.PubAdminDto;
import com.likelionsg13th.cardinal.pubOffice.dto.request.PubAdminLogin;
import com.likelionsg13th.cardinal.pubOffice.dto.request.UpdateNoticeAndDescripDto;
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
                 pubAdmin.getId()
                ,authService.issueToken(pubAdmin.getAdminId(), pubAdmin.getBooth().getId()));

    }

    @Transactional
    public UpdatePubResponse updateNoticeAndDescription(Long pubId, UserDetails principal, UpdateNoticeAndDescripDto  updateNoticeAndDescripDto) {
        PubBooth pub =  boothRepository.findPubBoothById(pubId).orElseThrow();

        String updatedNotice = updateNoticeAndDescripDto.getNotice();
        String updatedDescription = updateNoticeAndDescripDto.getDescription();

        pub.updateNoticeAndDescription(updatedNotice, updatedDescription);

        //dto 변환
        return UpdatePubResponse.of(pub);
    }

    //pubId와 pubAdmin 인가 확인
    public boolean canUpdate(UserDetails principal,Long pubId){
        PubAdmin pubAdmin =  pubAdminRepository.findByAdminId(principal.getUsername()).orElse(null);
        if(pubAdmin == null) return false;
        return pubAdmin.getBooth().getId().equals(pubId);

    }






}
