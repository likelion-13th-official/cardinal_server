package com.likelionsg13th.cardinal.map.repository;

import com.likelionsg13th.cardinal.map.domain.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Map,Long> {
}
