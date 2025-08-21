package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapDetailDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapListDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto;
import com.likelionsg13th.cardinal.common.exception.InvalidParameterException;
import com.likelionsg13th.cardinal.common.repository.AmenityRepository;

import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.likelionsg13th.cardinal.common.enums.ContentType.*;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.*;
import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class MapService {
    //범위 부스, 공연, 부대시설, 이벤트, 굿즈,
    private final AmenityRepository amenityRepository;
    private final GoodsRepository goodsRepository;
    private final PerformanceRepository performanceRepository;
    private final BoothRepository boothRepository;
    private final EventRepository eventRepository;
    private final ScrapRepository scrapRepository;
    private final UserRepository usersRepository;



    /*
    * AMENITY , GOODS, PERFORMANE : viewCount 추가.
    * */
    public MapDetailDto viewDetail(String category, Long AmenityId){

        String name , position;

        if(AmenityId != null && AMENITY.name().equalsIgnoreCase(category.trim())){

            Amenity amenity =  amenityRepository.findById(AmenityId).orElseThrow(() -> new InvalidParameterException("Amenity id not found"));
            name = amenity.getName();
            position = amenity.getLocation().getPosition();

        }else if(GOODS.name().equalsIgnoreCase(category.trim())){

            name = GOODS.toKorean();
            position = goodsRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new InvalidParameterException("Invalid Parameter : GOODS "))
                    .getLocation().getPosition();

        }else if (PERFORMANCE.name().equalsIgnoreCase(category.trim())){

            name = PERFORMANCE.toKorean();
            position = performanceRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new InvalidParameterException("Invalid Parameter : PERFORMANCE "))
                    .getLocation().getPosition();

        }else
            throw new InvalidParameterException("Invalid Parameter");

        return MapDetailDto .builder()
                .name(name)
                .position(position)
                .build();

    }

    /*
    * only for  FOOD_TRUCK , PUB
    *
    * */
//    public MapListDto viewList(String category,  Long locationId,UserPrincipal user){
//
//        System.out.println("-==========================");
//        System.out.println(user.getName());
//        System.out.println("-==========================");
//        List<Booth> boothList;
//        boolean bookMarked = false;
//
//
//        //모든 항목 받아오기.
//        if(locationId != null && FOOD_TRUCK.name().equalsIgnoreCase(category.trim())) {
//            boothList = boothRepository.findAllByCategoryAndLocation_Id(FOOD_TRUCK, locationId);
//
//        }else if(PUB.name().equalsIgnoreCase(category.trim())) {
//            boothList = boothRepository.findAllByCategory(PUB);
//        }else {
//            throw  new InvalidParameterException("category는 PUB,FOOD_TRUCK만 사용해주세요. ");
//        }
//
//            /*
//            * 1. 부스 id목록만 가져온다.
//            * 2. 1번으로 스크랩한 거 한번에 모아오기
//            * 3. 스크랩한 부스 id만 set으로 모으기
//            * 4. 비교해서 dto변환
//            * */
//        if(user != null){
//
//           List<Long> boothId =  boothList.stream().map(Booth::getId).toList();
//           Set<Long> ScrappedBoothId = scrapRepository.findContentIdsByUserIdAndContentIdsIn(user.getName());
//        }
//
//
//
//    }
//
//    public MapListDto viewList(String category,  Long locationId,UserPrincipal user){
//
//        System.out.println("-==========================");
//        System.out.println(user.getName());
//        System.out.println("-==========================");
//        List<Booth> boothList;
//        boolean bookMarked = false;
//
//
//        //모든 항목 받아오기.
//        if(locationId != null && FOOD_TRUCK.name().equalsIgnoreCase(category.trim())) {
//            boothList = boothRepository.findAllByCategoryAndLocation_Id(FOOD_TRUCK, locationId);
//
//        }else if(PUB.name().equalsIgnoreCase(category.trim())) {
//            boothList = boothRepository.findAllByCategory(PUB);
//        }else {
//            throw  new InvalidParameterException("category는 PUB,FOOD_TRUCK만 사용해주세요. ");
//        }
//
//            /*
//            * 1. 부스 id목록만 가져온다.
//            * 2. 1번으로 스크랩한 거 한번에 모아오기
//            * 3. 스크랩한 부스 id만 set으로 모으기
//            * 4. 비교해서 dto변환
//            * */
//        if(user != null){
//
//           List<Long> boothId =  boothList.stream().map(Booth::getId).toList();
//           Set<Long> ScrappedBoothId = scrapRepository.findContentIdsByUserIdAndContentIdsIn(user.getName());
//        }
//
//
//
//    }
//


}
