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

import java.util.List;
import java.util.Optional;

@Repository
public interface BoothRepository extends JpaRepository<Booth,Long>, JpaSpecificationExecutor<Booth> {


    List<Booth> findAllByCategoryAndLocationId(BoothCategory category, Long locationId);
    List<Booth> findAllByCategory(BoothCategory category);

    @Query("SELECT p FROM PubBooth p WHERE p.id = :id")
    Optional<PubBooth> findPubBoothById(@Param("id") Long id);


    /*이벤트맵 검색 : 라벨 = 매장 이름  */
    @Query("SELECT new com.likelionsg13th.cardinal.map.dto.MapSearchDto(" +
            "'BOOTH',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM Booth g WHERE g.name LIKE :keyword " +
            "AND " +
            "g.category NOT IN :excludeCategories")
    List<MapSearchDto> findAllByNameContainingAndCategoryIsNotContainingAndCategoryNotIn(
            @Param("keyword") String keyword,
            @Param("excludeCategories") List<BoothCategory> excludeCategories);

    /*이벤트맵 검색 : 라벨 = 위치명 또는 카테고리명   */
    @Query("SELECT DISTINCT new com.likelionsg13th.cardinal.map.dto.MapSearchDto (" +
            "'BOOTH',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM Booth g JOIN g.menus m " +
            "WHERE g.category IN :includeCategories  " +
            "AND " +
            "( g.name LIKE :keyword OR m.name LIKE :keyword) ")
    List<MapSearchDto>  findAllByNameContainingAndMenusContainingAndCategoryIn(@Param("keyword") String keyword, @Param("includeCategories") List<BoothCategory> includeCategories);

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
