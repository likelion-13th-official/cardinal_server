package com.likelionsg13th.cardinal.performance.repository;


import com.likelionsg13th.cardinal.common.enums.PerformanceCategory;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
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

    List<Performance> findAllByNameLike(@Param("keyword") String keyword);

    @Query("SELECT p.location" +
            " FROM Performance p " +
            " WHERE p.id = 1")
    Map findLocationFirstById();

    List<Performance> findByCategory(PerformanceCategory category);

}
