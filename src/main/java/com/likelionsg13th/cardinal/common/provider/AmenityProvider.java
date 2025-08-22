package com.likelionsg13th.cardinal.common.provider;


import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapFilteredByCategoryDetailDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapFilteredByCategoryItemDto;
import com.likelionsg13th.cardinal.common.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import static com.likelionsg13th.cardinal.common.enums.ContentType.AMENITY;

@Component
@RequiredArgsConstructor
public class AmenityProvider implements CategoryProvider{
    private final AmenityRepository amenityRepository;

    @Override
    public boolean hasCategory(String category){
        return AMENITY.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory(){

        List<MapFilteredByCategoryItemDto> items = amenityRepository.findAll().stream().map(
                amenity ->
                        MapFilteredByCategoryItemDto.from(
                                amenity.getName(),
                                amenity.getId(),
                                amenity.getLocation())
        ).toList();


        return MapFilteredByCategoryDetailDto.of(AMENITY.name(), items);
    }
}
