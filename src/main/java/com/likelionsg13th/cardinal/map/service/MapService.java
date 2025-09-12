package com.likelionsg13th.cardinal.map.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.enums.PerformanceCategory;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.*;
import com.likelionsg13th.cardinal.common.provider.CategoryProvider;
import com.likelionsg13th.cardinal.common.provider.ProviderFactory;
import com.likelionsg13th.cardinal.common.repository.AmenityRepository;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.goods.repository.GoodsRepository;
import com.likelionsg13th.cardinal.map.exception.LocationNotProvidedForFoodTruck;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.service.ScrapService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.likelionsg13th.cardinal.common.enums.BoothCategory.FOOD_TRUCK;
import static com.likelionsg13th.cardinal.common.enums.BoothCategory.PUB;
import static com.likelionsg13th.cardinal.common.enums.ContentType.*;
import static com.likelionsg13th.cardinal.common.utils.EnumUtil.boothCategoryValueOfIgnoreCase;

@Service
@RequiredArgsConstructor
public class MapService {

    private final ProviderFactory providerFactory;
    private final ScrapService scrapService;

    private final AmenityRepository amenityRepository;
    private final GoodsRepository goodsRepository;
    private final PerformanceRepository performanceRepository;
    private final BoothRepository boothRepository;
    private final EventRepository eventRepository;

    @Getter
    @AllArgsConstructor
    private static class LocationItemHolder {
        private Map location;
        private MapSearchItemDto item;
    }

    public Object getMapMarkersByCategory(String category) {
        CategoryProvider categoryProvider = providerFactory.getProvider(category);
        return categoryProvider.getMapMarkersByCategory();
    }

    public List<MapSearchResponseDto> getSearchResult(String keyword) {
        String searchKeyword = "%" + keyword + "%";
        System.out.println(searchKeyword+"!!!!!!!!!!!!!!!!!");

        List<Goods> goods = goodsRepository.findAllByNameLike(searchKeyword);
        List<Event> events = eventRepository.findAllByNameLike(searchKeyword);
        List<Performance> performances = performanceRepository.findAllByNameLike(searchKeyword);
        List<Booth> pubAndFoodTrucks = boothRepository.findAllByNameLikeOrMenusNameLikeAndCategoryIn(searchKeyword, List.of(PUB, FOOD_TRUCK));
        List<Booth> otherBooths = boothRepository.findAllByNameLikeAndCategoryNotIn(searchKeyword, List.of(PUB, FOOD_TRUCK));

        System.out.println(pubAndFoodTrucks.size() + "==============" + otherBooths.size() );

        List<Booth> allBooths = Stream.concat(pubAndFoodTrucks.stream(), otherBooths.stream()).distinct().toList();

        System.out.println(allBooths.size()+"===================");
        List<LocationItemHolder> allItems = new ArrayList<>();

        goods.forEach(g -> {
            MapSearchDetail detail = MapSearchPriceDetailDto.from(g);
            MapSearchItemDto item = MapSearchItemDto.of(g, detail);
            allItems.add(new LocationItemHolder(g.getLocation(), item));
        });

        events.forEach(e -> {
            MapSearchDetail detail = MapSearchTimeDetailDto.from(e);
            MapSearchItemDto item = MapSearchItemDto.of(e, detail);
            allItems.add(new LocationItemHolder(e.getLocation(), item));
        });

        performances.forEach(p -> {
            MapSearchDetail detail = null;
            if (p.getCategory() == PerformanceCategory.FILM) {
                detail = MapSearchTimeDetailDto.from(p);
            }
            MapSearchItemDto item = MapSearchItemDto.of(p, detail);
            allItems.add(new LocationItemHolder(p.getLocation(), item));
        });

        allBooths.forEach(b -> {
            MapSearchDetail detail = MapSearchTimeDetailDto.from(b);
            MapSearchItemDto item = MapSearchItemDto.of(b, detail);
            allItems.add(new LocationItemHolder(b.getLocation(), item));
        });

        java.util.Map<Map, List<LocationItemHolder>> groupedByLocation = allItems.stream()
                .collect(Collectors.groupingBy(LocationItemHolder::getLocation));

        return groupedByLocation.entrySet().stream()
                .map(entry -> {
                    Map location = entry.getKey();
                    List<MapSearchItemDto> items = entry.getValue().stream()
                            .map(LocationItemHolder::getItem)
                            .collect(Collectors.toList());

                    return MapSearchResponseDto.builder()
                            .position(location.getPosition())
                            .longitude(location.getLongitude())
                            .latitude(location.getLatitude())
                            .booths(items)
                            .build();
                })
                .collect(Collectors.toList());
    }


    public MapDetailDto getDetail(String category, Long AmenityId) {
        String name, position;

        if (AmenityId != null && AMENITY.name().equalsIgnoreCase(category.trim())) {
            Amenity amenity = amenityRepository.findById(AmenityId).get();
            name = amenity.getName();
            position = amenity.getLocation().getPosition();
        } else if (GOODS.name().equalsIgnoreCase(category.trim())) {
            name = GOODS.toKorean();
            position = goodsRepository.findFirstByOrderByIdAsc()
                    .get()
                    .getLocation().getPosition();
        } else if (PERFORMANCE.name().equalsIgnoreCase(category.trim())) {
            name = PERFORMANCE.toKorean();
            position = performanceRepository.findFirstByOrderByIdAsc()
                    .get()
                    .getLocation().getPosition();
        } else
            throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);

        return MapDetailDto.builder()
                .name(name)
                .position(position)
                .build();
    }


    public MapListDto getList(String category, Long locationId, UserDto user) {
        BoothCategory boothCategory = boothCategoryValueOfIgnoreCase(category);
        List<Booth> boothList = findBoothListFor(boothCategory, locationId);
        if (boothList.isEmpty()) {
            return MapListDto.of(null, boothCategory.name(), null, false);
        }

        Set<Long> bookMarkedBoothIds = scrapService.getBookMarkedBoothIds(boothList, user);

        List<MapListItemDto> itemList = boothList.stream().map(
                booth -> {
                    boolean bookMarked = bookMarkedBoothIds.contains(booth.getId());
                    return MapListItemDto.from(booth, bookMarked);
                }
        ).toList();

        String position = boothList.get(0).getLocation().getPosition();
        return MapListDto.of(itemList, boothCategory.name(), position);
    }


    private List<Booth> findBoothListFor(BoothCategory boothCategory, Long locationId) {
        return switch (boothCategory) {
            case PUB -> boothRepository.findAllByCategory(boothCategory);
            case FOOD_TRUCK -> {
                if (locationId == null)
                    throw new LocationNotProvidedForFoodTruck(ErrorCode.LOCATION_NOT_PROVIDED_FOR_FOOD_TRUCK);
                yield boothRepository.findAllByCategoryAndLocationId(boothCategory, locationId);
            }
            default -> throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
        };
    }
}