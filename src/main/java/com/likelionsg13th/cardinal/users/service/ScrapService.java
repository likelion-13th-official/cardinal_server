package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.ScrapNotFoundException;
import com.likelionsg13th.cardinal.common.exception.UserNotFoundException;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    private final UserService userService;

    public void addScrap(UserDto userDto,String category,Long categoryId) {


    }

    /*scrapId로 스크랩 해제 */
    public void deleteScrapByScrapId(UserDto user,Long scrapId) {
        if(user == null) throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        /*존재하지 않는 스크랩일 시 에러 반환*/
        if(!scrapRepository.existsByIdAndUserId(scrapId,user.getId()))
            throw new ScrapNotFoundException(ErrorCode.SCRAP_NOT_FOUND);

        scrapRepository.deleteById(scrapId);

    }

    @Transactional
    public void DeleteAllScrapByUserId(UserDto user) {
        if(user == null) throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        scrapRepository.deleteAllByUserId(user.getId());

    }
}
