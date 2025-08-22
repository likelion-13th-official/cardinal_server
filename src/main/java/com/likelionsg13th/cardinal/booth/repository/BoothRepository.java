package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothRepository extends JpaRepository<Booth,Long> {
    List<Booth> findAllByCategoryAndLocation_Id(BoothCategory category, Long locationId);
    List<Booth> findAllByCategory(BoothCategory category);
    List<Booth> findByCategory(BoothCategory category);
    List<Booth> findAllByNameContaining(String name);

    /*메뉴 이름+부스 이름으로 검색*/
    @Query("SELECT DISTINCT b FROM Booth b LEFT JOIN b.menus m " +
            "WHERE b.name LIKE %:query% OR m.name LIKE %:query%")
    Page<Booth> findByNameOrMenuNameContaining(@Param("query") String query, Pageable pageable);


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
    List<MapSearchDto> findAllByNameContainingAndCategoryIsNotContaining(
            @Param("keyword") String keyword,
            @Param("excludeCategories") List<BoothCategory> excludeCategories);
}
