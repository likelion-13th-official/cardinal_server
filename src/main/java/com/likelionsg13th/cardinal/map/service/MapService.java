package com.likelionsg13th.cardinal.map.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.common.exception.ParameterIsNullOrEmpty;
import com.likelionsg13th.cardinal.map.dto.*;
import com.likelionsg13th.cardinal.common.exception.InvalidParameterException;
import com.likelionsg13th.cardinal.common.provider.CategoryProvider;
import com.likelionsg13th.cardinal.common.provider.ProviderFactory;
import com.likelionsg13th.cardinal.common.repository.AmenityRepository;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import static com.likelionsg13th.cardinal.common.enums.ContentType.*;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.*;

@Service
@RequiredArgsConstructor
public class MapService {

    private final ProviderFactory providerFactory;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;

    // 범위 부스, 공연, 부대시설, 이벤트, 굿즈,
    private final AmenityRepository amenityRepository;
    private final GoodsRepository goodsRepository;
    private final PerformanceRepository performanceRepository;
    private final BoothRepository boothRepository;
    private final EventRepository eventRepository;


    /*
    * foodtruck,pub,PERFORMANCE,굿즈샵 -> 빌딩 정보
    * 마당사업, 포토부스, 제휴 , 이벤트 , 부대시설  -> 상세 위치 정보
    * */
    public Object getMapMarkersByCategory(String category){

        if(category == null ||category.isEmpty()) throw new ParameterIsNullOrEmpty(ErrorCode.PARAMETER_IS_NULL_OR_EMPTY);

        CategoryProvider categoryProvider = providerFactory.getProvider(category);

        return categoryProvider.getMapMarkersByCategory();


    }



    /*
       TODO : keyword null 에러 처리
     * 검색 범위 :
     * 굿즈 : 제품 명,
     * 이벤트 : 이벤트 명,
     * 부대시설 : 부대시설 명
     * 공연 : 공연 명
     * 부스 : 부스 명
     * ->   주점 , 푸드트럭 : + 대표 메뉴
     * */
    public List<MapSearchDto> getSearchResult(String keyword){
        if(keyword == null ||keyword.isEmpty()) throw new ParameterIsNullOrEmpty(ErrorCode.PARAMETER_IS_NULL_OR_EMPTY);

        String searchKeyword = "%"+keyword+"%";
        Stream<List<MapSearchDto>> streams = Stream.of(
                goodsRepository.findAllByNameContaining(searchKeyword),
                eventRepository.findAllByNameContaining(searchKeyword),
                amenityRepository.findAllByNameContaining(searchKeyword),
                performanceRepository.findAllByNameContaining(searchKeyword),
                boothRepository.findAllByNameContainingAndMenusContainingAndCategoryIn(searchKeyword,List.of(PUB,FOOD_TRUCK)),
                boothRepository.findAllByNameContainingAndCategoryIsNotContainingAndCategoryNotIn(searchKeyword,List.of(PUB,FOOD_TRUCK))  //부스 내 주점,푸드트럭 별도 처리 제외
        );


        return streams
                .flatMap(Collection::stream)
                .toList();
    }


    /*
    TODO : 1. viewcount 추가
           2. 에러 처리 수정
    */
    public MapDetailDto getDetail(String category, Long AmenityId){
        if(category == null ||category.isEmpty()) throw new ParameterIsNullOrEmpty(ErrorCode.PARAMETER_IS_NULL_OR_EMPTY);

        String name , position;

        if(AmenityId != null && AMENITY.name().equalsIgnoreCase(category.trim())){

            Amenity amenity =  amenityRepository.findById(AmenityId).get();
            name = amenity.getName();
            position = amenity.getLocation().getPosition();

        }else if(GOODS.name().equalsIgnoreCase(category.trim())){

            name = GOODS.toKorean();
            position = goodsRepository.findFirstByOrderByIdAsc()
                    .get()
                    .getLocation().getPosition();

        }else if (PERFORMANCE.name().equalsIgnoreCase(category.trim())){

            name = PERFORMANCE.toKorean();
            position = performanceRepository.findFirstByOrderByIdAsc()
                    .get()
                    .getLocation().getPosition();

        }else
            throw new InvalidParameterException(ErrorCode.INVALID_CATEGORY);

        return MapDetailDto .builder()
                .name(name)
                .position(position)
                .build();

    }



    /*
    * only for  FOOD_TRUCK , PUB
    * TODO :1.  Scrap 구현 하기
    *       2.  에러 처리
    *
    * */
    public MapListDto getList(String category, Long locationId, UserDto user ){
        if(category == null || category.isEmpty()) throw new ParameterIsNullOrEmpty(ErrorCode.PARAMETER_IS_NULL_OR_EMPTY);

        String position;
        List<Booth> boothList;
        List<MapListItemDto> mapListItemDtoList;


        if(locationId != null && FOOD_TRUCK.name().equalsIgnoreCase(category.trim())) {
            boothList = boothRepository.findAllByCategoryAndLocationId(FOOD_TRUCK, locationId);
            position  = boothList.get(0).getLocation().getPosition();
        }else if(PUB.name().equalsIgnoreCase(category.trim())) {
            boothList = boothRepository.findAllByCategory(PUB);
            position  = boothList.get(0).getLocation().getPosition();
        }else {
            throw  new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
        }

        if(user != null){
            System.out.println("USER IN ");
            mapListItemDtoList =  boothList.stream().map(
                    booth -> {
                       boolean bookMarked =  scrapRepository.existsByUser_IdAndContentIdAndContentType(user.getId(),booth.getId(),BOOTH);
                       System.out.println("bookMarked "+bookMarked);
                       return   MapListItemDto.from(booth,bookMarked);
                    }
            ).toList();

            return MapListDto.of(mapListItemDtoList,category,position);

        }else {
            return MapListDto.of(boothList,category,position,false);
        }


    }





}
