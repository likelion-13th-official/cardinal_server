package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.BoothCategory.PUB;


/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class PubProvider implements CategoryProvider {

    private final BoothRepository pubBoothRepository;

    @Override
    public boolean hasCategory(String category){
        return PUB.name().equalsIgnoreCase(category);
    }



    @Override
    public Object getMapMarkersByCategory() {
        MapInfoDto mapInfo = MapInfoDto.from(pubBoothRepository.findLocationFirstByIdAndCategory(PUB));

        return MapFilteredByCategoryDto.from(List.of(mapInfo),PUB.name());

    }
}
