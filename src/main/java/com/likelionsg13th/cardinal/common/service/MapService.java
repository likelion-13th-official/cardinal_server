package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.PubBooth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapDetailDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapListDetailDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapListDto;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.exception.InvalidParameterException;
import com.likelionsg13th.cardinal.common.repository.AmenityRepository;

import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.likelionsg13th.cardinal.common.enums.ContentType.*;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.*;
import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class MapService {

    private final AmenityRepository amenityRepository;
    private final GoodsRepository goodsRepository;
    private final PerformanceRepository performanceRepository;
    private final BoothRepository boothRepository;
    private final ScrapRepository scrapRepository;
    private final UsersRepository usersRepository;

    public MapDetailDto viewDetail(String category, Long AmenityId){

        String name , position;

        if(AmenityId != null && AMENITY.name().equalsIgnoreCase(category.trim())){

            Amenity amenity =  amenityRepository.findById(AmenityId).orElseThrow(() -> new InvalidParameterException("Amenity id not found"));
            name = amenity.getName();
            position = amenity.getLocation().getPosition();

        }else if(GOODS.name().equalsIgnoreCase(category.trim())){

            name = GOODS.toKorean();
            position = goodsRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new InvalidParameterException("Invalid Parameter : GOODS "))
                    .getLocation().getPosition();

        }else if (PERFORMANCE.name().equalsIgnoreCase(category.trim())){

            name = PERFORMANCE.toKorean();
            position = performanceRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new InvalidParameterException("Invalid Parameter : PERFORMANCE "))
                    .getLocation().getPosition();

        }else
            throw new InvalidParameterException("Invalid Parameter");

        return MapDetailDto .builder()
                .name(name)
                .position(position)
                .build();

    }

    /*
    * only for  FOOD_TRUCK , PUB
    *
    * */
    public MapListDto viewList(String category, Long locationId){
        List<Booth> boothList;

        if(locationId != null && FOOD_TRUCK.name().equalsIgnoreCase(category.trim())) {
            boothList = boothRepository.findAllByCategoryAndLocation_Id(FOOD_TRUCK, locationId);
        }else if(PUB.name().equalsIgnoreCase(category.trim())) {
            boothList = boothRepository.findAllByCategory(PUB);
        }else {
            throw  new InvalidParameterException("category는 PUB,FOOD_TRUCK만 사용해주세요. ");
        }

        //detailDto -> listDto
        return MapListDto.from(boothList,category);



    }




}
