package com.likelionsg13th.cardinal.performance.repository;

import com.likelionsg13th.cardinal.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findFirstByOrderByIdAsc();

    List<Performance> findAllByNameContaining(String name);

}
