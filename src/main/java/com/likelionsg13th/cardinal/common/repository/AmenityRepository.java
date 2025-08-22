package com.likelionsg13th.cardinal.common.repository;

import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity,Long> {

    @Query("SELECT new com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto(" +
            "'AMENITY',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM Amenity g WHERE g.name LIKE :keyword")
    List<MapSearchDto> findAllByNameContaining(@Param("keyword") String keyword);
}
