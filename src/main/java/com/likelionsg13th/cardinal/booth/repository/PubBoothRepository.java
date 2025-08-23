package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PubBoothRepository extends JpaRepository<PubBooth, Long> {

    @Query("SELECT DISTINCT new com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto(" +
            "'BOOTH',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM PubBooth g JOIN g.menus m  WHERE g.name LIKE :keyword OR m.name LIKE :keyword ")
    List<MapSearchDto>  findAllByNameContainingAndMenusContaining(@Param("keyword") String keyword);
}
