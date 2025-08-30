package com.likelionsg13th.cardinal.map.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.common.exception.LocationNotProvidedForFoodTruck;
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
import com.likelionsg13th.cardinal.users.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.likelionsg13th.cardinal.common.enums.ContentType.*;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.*;
import static com.likelionsg13th.cardinal.common.utils.EnumUtil.boothCategoryValueOfIgnoreCase;

@Service
@RequiredArgsConstructor
public class MapService {

    private final ProviderFactory providerFactory;
    private final ScrapService scrapService;

    // 범위 부스, 공연, 부대시설, 이벤트, 굿즈,
    private final AmenityRepository amenityRepository;
    private final GoodsRepository goodsRepository;
    private final PerformanceRepository performanceRepository;
    private final BoothRepository boothRepository;
    private final EventRepository eventRepository;


    /*
    * 부스(foodtruck,pub),PERFORMANCE,굿즈샵 -> 빌딩 정보
    * 부스(마당사업, 포토부스, 제휴) , 이벤트 , 부대시설  -> 상세 위치 정보
    * */
    public Object getMapMarkersByCategory(String category){

        CategoryProvider categoryProvider = providerFactory.getProvider(category);

        return categoryProvider.getMapMarkersByCategory();
    }



    /*
       TODO : keyword null 에러 처리으로
     * 검색 범위 :
     * 굿즈 : 제품 명,
     * 이벤트 : 이벤트 명,
     * 부대시설 : 부대시설 명
     * 공연 : 공연 명
     * 부스 : 부스 명
     * ->   주점 , 푸드트럭 : + 대표 메뉴
     * */
    public List<MapSearchDto> getSearchResult(String keyword){


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
           3. 리팩토링 (분기문)
    */
    public MapDetailDto getDetail(String category, Long AmenityId){

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
    * 특정 카테고리(푸드트럭, 주점)에 속한 부스 목록을 스크랩 여부와 함께 반환
    * @param category 조회할 부스 카테고리 ( FOOD_TRUCK, PUB)
    * @param locationId 'FOOD_TRUCK' 카테고리 한해서 특정 POSITION ID (OPTIONAL)
    * @param user 현재 로그인 유저 정보 (OPTIONAL) , null 인경우 scrap = false 처리.
    * @return 부스 목록 + 위치 + 스크랩 여부
     */
    public MapListDto getList(String category, Long locationId, UserDto user ){
        /*category Enum 처리 */
        BoothCategory boothCategory = boothCategoryValueOfIgnoreCase(category);
        /*카테고리 & 위치 별 부스 목록 조회 메서드 */
        List<Booth> boothList = findBoothListFor(boothCategory, locationId);
        /*Data 없을 시 빈 DTO 반환*/
        if(boothList.isEmpty()){return MapListDto.of(null,boothCategory.name(),null,false);}

        /*유저가 스크랩 한 부스 ID Set*/
        Set<Long> bookMarkedBoothIds = scrapService.getBookMarkedBoothIds(boothList,user);

        /*스크랩 여부 확인하여 dto 생성*/
        List<MapListItemDto> itemList = boothList.stream().map(
                booth -> {
                    boolean bookMarked = bookMarkedBoothIds.contains(booth.getId());
                    return MapListItemDto.from(booth,bookMarked);
                }

        ).toList();

        /*FOOD_TRUCK,PUB 모두 동일한 위치 이므로 첫 번째 객체의 위치 반환*/
        String position  = boothList.get(0).getLocation().getPosition();
        return MapListDto.of(itemList,boothCategory.name(),position);

    }



    /*
     * @param boothList 스크랩 여부 확인할 부스 리스트
     * @param locationId FOOD_TRUCK 한하여 사용. location 별 리스트 반환
     */
    private List<Booth> findBoothListFor(BoothCategory boothCategory,Long locationId){

        return switch (boothCategory){
            case PUB ->  boothRepository.findAllByCategory(boothCategory);
            case FOOD_TRUCK -> {
                if(locationId == null)
                    throw new LocationNotProvidedForFoodTruck(ErrorCode.LOCATION_NOT_PROVIDED_FOR_FOOD_TRUCK);
                yield boothRepository.findAllByCategoryAndLocationId(boothCategory,locationId);
            }
            default ->  throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
        };


    }





}
