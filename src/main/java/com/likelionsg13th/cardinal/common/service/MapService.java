package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapDetailDto;
import com.likelionsg13th.cardinal.common.exception.InvalidParameterException;
import com.likelionsg13th.cardinal.common.repository.AmenityRepository;
import com.likelionsg13th.cardinal.common.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapService {

    private  final MapRepository mapRepository;
    private final AmenityRepository amenityRepository;
    private final BoothRepository boothRepository;


    public MapDetailDto viewDetail(String category, Long AmenityId){




        if("AMENITY".equals(category)){ //enum으로 수정
            Optional<Amenity> amenity =  amenityRepository.findById(AmenityId);

            if(amenity.isPresent()){
                return MapDetailDto .builder()
                        .name(amenity.get().getName())
                        .position(amenity.get().getLocation().getPosition())
                        .build();
            }else{
                throw new InvalidParameterException("해당 ID의 부대시설이 존재하지 않습니다.");
            }
        }else{
            //boothRepository.findByCategory(keyword);
            return MapDetailDto .builder()
                    .name(category)
                    .position("test")
                    .build();
        }

    }

}
