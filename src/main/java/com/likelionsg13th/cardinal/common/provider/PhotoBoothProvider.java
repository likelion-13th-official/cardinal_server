package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDetailDto;
import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.BoothCategory.PHOTO_BOOTH;

/*
 * CASE 2
 * */
@Component
@RequiredArgsConstructor
public class PhotoBoothProvider implements CategoryProvider{
    private final BoothRepository photoBoothRepository;


    @Override
    public boolean hasCategory(String category){
        return PHOTO_BOOTH.name().equalsIgnoreCase(category);
    }

    @Override
    public Object getMapMarkersByCategory() {

        List<MapFilteredByCategoryItemDto> items = photoBoothRepository.findAllByCategory(PHOTO_BOOTH).stream().map(
                photoBooth ->
                        MapFilteredByCategoryItemDto.from(
                                photoBooth.getName(),
                                photoBooth.getId(),
                                photoBooth.getLocation())
        ).toList();


        return MapFilteredByCategoryDetailDto.of(PHOTO_BOOTH.name(), items);
    }
}
