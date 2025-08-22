package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import static com.likelionsg13th.cardinal.common.enums.ContentType.GOODS;

/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class GoodsProvider implements CategoryProvider {

    private final GoodsRepository goodsRepository;

    @Override
    public boolean hasCategory(String category) {
        return  GOODS.name().equalsIgnoreCase(category);
    }
    /*
     * 굿즈는 공통 위치 가지므로 1개 반환
     */
    @Override
    public Object getMapMarkersByCategory() {
        MapInfoDto mapInfo = MapInfoDto.from(goodsRepository.findLocationFirstById());

        return MapFilteredByCategoryDto.from(List.of(mapInfo),GOODS.name());
    }
}
