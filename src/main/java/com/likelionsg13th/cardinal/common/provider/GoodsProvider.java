package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.BoothCategory.PUB;
import static com.likelionsg13th.cardinal.common.enums.ContentType.GOODS;

@Component
@RequiredArgsConstructor
public class GoodsProvider implements CategoryProvider {

    private final GoodsRepository goodsRepository;

    @Override
    public boolean hasCategory(String category) {
        return  GOODS.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory() {
        MapInfoDto mapInfo = MapInfoDto.from(goodsRepository.findLocationFirstById());

        return MapFilteredByCategoryDto.from(List.of(mapInfo),GOODS.name());
    }
}
