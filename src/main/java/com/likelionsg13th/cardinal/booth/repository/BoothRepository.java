package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface BoothRepository extends JpaRepository<Booth,Long>, JpaSpecificationExecutor<Booth> {

    //운영을 시작해야할 부스
    List<Booth> findByOperatingInfo_IsOperatingFalseAndOperatingDaysContainingAndOperatingInfo_StartTimeLessThanEqualAndOperatingInfo_EndTimeAfter(
            DayOfWeek dayOfWeek, LocalTime currentTimeForStart, LocalTime currentTimeForEnd);

    // 2. 운영을 종료해야 할 부스
    List<Booth> findByOperatingInfo_IsOperatingTrueAndOperatingDaysNotContainingOrOperatingInfo_IsOperatingTrueAndOperatingInfo_EndTimeLessThanEqual(
            DayOfWeek dayOfWeek, LocalTime currentTime);

    Optional<List<Booth>> findAllByCategoryAndLocationId(BoothCategory category, Long locationId);
    List<Booth> findAllByCategory(BoothCategory category);

    @Query("SELECT p FROM PubBooth p WHERE p.id = :id")
    Optional<PubBooth> findPubBoothById(@Param("id") Long id);


    /*이벤트맵 검색 : 라벨 = 매장 이름  */
    @Query("SELECT g FROM Booth g WHERE g.name LIKE :keyword " +
            "AND " +
            "g.category NOT IN :excludeCategories")
    List<Booth> findAllByNameLikeAndCategoryNotIn(
            @Param("keyword") String keyword,
            @Param("excludeCategories") List<BoothCategory> excludeCategories);

    /*이벤트맵 검색 : 라벨 = 위치명 또는 카테고리명   */
    @Query("SELECT DISTINCT g " +
            "FROM Booth g JOIN g.menus m " +
            "WHERE g.category IN :includeCategories  " +
            "AND " +
            "( g.name LIKE :keyword OR m.name LIKE :keyword) ")
    List<Booth> findAllByNameLikeOrMenusNameLikeAndCategoryIn(@Param("keyword") String keyword, @Param("includeCategories") List<BoothCategory> includeCategories);

    /* 이벤트맵 카테고리 필터 : 공통 위치 1개 반환 */
    @Query("SELECT p.location" +
            " FROM Booth p " +
            " WHERE p.id = 1 AND p.category = :category")
    Map findLocationFirstByIdAndCategory(@Param("category") BoothCategory category);

    /* 이벤트맵 카테고리 필터 : 매장 별 위치 반환 */
    @Query("SELECT DISTINCT p.location " +
            "FROM Booth p " +
            "WHERE p.category = :category")
    List<Map> findLocationAllDistinctByCategory(@Param("category") BoothCategory category);
}
