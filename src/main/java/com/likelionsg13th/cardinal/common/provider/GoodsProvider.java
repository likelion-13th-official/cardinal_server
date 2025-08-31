package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.EventNotFound;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapCommonDto;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapGoodsDetailDto;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapTimeDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

import static com.likelionsg13th.cardinal.common.enums.ContentType.GOODS;
import static com.likelionsg13th.cardinal.common.enums.DayOfWeek.ALWAYS;

/*
 * CASE 1
 * */
@Component
@RequiredArgsConstructor
public class GoodsProvider implements CategoryProvider,Scrappable {

    private final GoodsRepository goodsRepository;

    @Override
    public boolean hasCategory(String category) {
        return  GOODS.name().equalsIgnoreCase(category);
    }

    @Override
    public Optional<ScrapCommonDto> getScrapCommonDto(Long contentId) {
        Optional<Goods> goodsOptional = goodsRepository.findById(contentId);
        if(goodsOptional.isEmpty()) return Optional.empty();
        Goods goods  = goodsOptional.get();

        return Optional.of(ScrapCommonDto.of(goods, ScrapGoodsDetailDto.from(goods)));
    }


    /*
     * 굿즈는 공통 위치 가지므로 1개 반환
     */
    @Override
    public Object getMapMarkersByCategory() {
        MapInfoDto mapInfo = MapInfoDto.from(goodsRepository.findLocationFirstById());

        return MapFilteredByCategoryDto.from(List.of(mapInfo),GOODS.name());
    }

    @Override
    public ContentType ValidateContentExistsForScrap(Long categoryId) {
        if(!goodsRepository.existsById(categoryId))
            throw new EventNotFound(ErrorCode.GOODS_NOT_FOUND);
        return GOODS;
    }
}
