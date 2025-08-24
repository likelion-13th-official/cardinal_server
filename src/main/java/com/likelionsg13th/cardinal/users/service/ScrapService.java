package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    private final UserService userService;
    public boolean deleteScrapByUserId(UserDetails userDetails,Long scrapId) {
        //scrap의 주인과, 받아온 유저 정보 일치하는 로직 구현 (인가)

        UserDto user =  userService.getMeBySubject(userDetails.getUsername());

        return scrapRepository.deleteByUserId(user.getId());
    }
}
