package com.likelionsg13th.cardinal.performance.repository;

import com.likelionsg13th.cardinal.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findFirstByOrderByIdAsc();

    List<Performance> findAllByNameContaining(String name);

}
