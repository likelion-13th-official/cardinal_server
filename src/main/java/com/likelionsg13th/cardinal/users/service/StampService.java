package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.users.domain.Stamp;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.exception.StampDuplicateException;
import com.likelionsg13th.cardinal.users.exception.UserNotFoundException;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.response.StampResponse;
import com.likelionsg13th.cardinal.users.repository.StampRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StampService {
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    /* 스탬프 생성*/
    public StampResponse createStamp(UserDto userDto, ActivityType activityType){
        //e: 유저 없음
        Users user=userRepository.findById(userDto.getId())
                .orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        //e: 이미 적립한 스탬프
        if(stampRepository.existsByUserAndActivityType(user,activityType)){
            throw new StampDuplicateException(ErrorCode.STAMP_ALREADY_EXIST);
        }
        Stamp stamp=Stamp.builder()
                .user(user)
                .activityType(activityType)
                .build();

        Stamp savedStamp=stampRepository.save(stamp);
        return StampResponse.of(savedStamp);
    }


}
