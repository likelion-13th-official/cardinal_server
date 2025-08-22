package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PubBoothRepository extends JpaRepository<PubBooth, Long> {


    @Query("SELECT DISTINCT new com.likelionsg13th.cardinal.map.dto.MapSearchDto (" +
            "'BOOTH',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM PubBooth g JOIN g.menus m  WHERE g.name LIKE :keyword OR m.name LIKE :keyword ")
    List<MapSearchDto>  findAllByNameContainingAndMenusContaining(@Param("keyword") String keyword);


    @Query("SELECT p.location" +
            " FROM PubBooth p " +
            " WHERE p.id = 1")
    Map findLocationFirstById();

}
