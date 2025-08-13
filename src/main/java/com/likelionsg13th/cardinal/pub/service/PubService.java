package com.likelionsg13th.cardinal.pub.service;

import com.likelionsg13th.cardinal.pub.dto.resonseDto.PubsResponseDto;
import com.likelionsg13th.cardinal.pub.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;
    //주점 전체 조회
    public void getPubs(int page){

    }

    //주전 상세 조회
    public void getPubDetail(Long id ){

    }
}
