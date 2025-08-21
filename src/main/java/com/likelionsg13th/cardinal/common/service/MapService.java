package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.domain.Map;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapDetailDto;
import com.likelionsg13th.cardinal.common.exception.InvalidParameterException;
import com.likelionsg13th.cardinal.common.repository.AmenityRepository;
import com.likelionsg13th.cardinal.common.repository.MapRepository;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static com.likelionsg13th.cardinal.common.enums.ContentType.*;

@Service
@RequiredArgsConstructor
public class MapService {

    private final AmenityRepository amenityRepository;
    private final GoodsRepository goodsRepository;
    private final PerformanceRepository performanceRepository;


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


}
