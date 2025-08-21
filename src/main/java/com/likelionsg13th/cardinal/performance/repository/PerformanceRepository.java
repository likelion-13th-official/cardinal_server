package com.likelionsg13th.cardinal.performance.repository;

import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findFirstByOrderByIdAsc();

    @Query("SELECT new com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto(" +
            "'PERFORMANCE',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM Performance g WHERE g.name LIKE :keyword")
    List<MapSearchDto> findAllByNameContaining(@Param("keyword") String keyword);

}
