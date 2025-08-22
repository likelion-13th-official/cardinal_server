package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.domain.Map;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapInfoDto;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.likelionsg13th.cardinal.booth.repository.PubBoothRepository;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.PUB;

@Component
@RequiredArgsConstructor
public class PubProvider implements CategoryProvider {

    private final PubBoothRepository pubBoothRepository;

    @Override
    public boolean hasCategory(String category){
        return PUB.name().equalsIgnoreCase(category);
    }

    /*
    * 주점은 공통 위치 가지므로 1개 반환
    */
    @Override
    public Object getMapMarkersByCategory() {
        MapInfoDto mapInfo = MapInfoDto.from(pubBoothRepository.findMapFirstByLocation());

        return MapFilteredByCategoryDto.from(List.of(mapInfo),PUB.name());

    }
}
