//package com.likelionsg13th.cardinal.common.provider;
//
//import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
//import com.likelionsg13th.cardinal.common.enums.BoothCategory;
//import com.likelionsg13th.cardinal.map.dto.MapFilteredByCategoryDto;
//import com.likelionsg13th.cardinal.map.dto.MapInfoDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//import static com.likelionsg13th.cardinal.common.enums.BoothCategory.PUB;
//import static com.likelionsg13th.cardinal.common.utils.EnumUtil.boothCategoryValueOfIgnoreCase;
//
//@Component
//@RequiredArgsConstructor
//public class BoothProvider implements CategoryProvider {
//    private final BoothRepository BoothRepository;
//
//    @Override
//    public boolean hasCategory(String category){
//        return PUB.name().equalsIgnoreCase(category);
//    }
//
//    @Override
//    public Object getMapMarkersByCategory() {
//        return null;
//    }
//
//
//    @Override
//    public Object getMapMarkersByCategory(String category) {
//        BoothCategory boothCategory= boothCategoryValueOfIgnoreCase(category);
//
//        MapInfoDto mapInfo = MapInfoDto.from(BoothRepository.findLocationFirstByIdAndCategory(boothCategory));
//
//        return MapFilteredByCategoryDto.from(List.of(mapInfo),boothCategory.name());
//
//    }
//
//
////
//
//}
