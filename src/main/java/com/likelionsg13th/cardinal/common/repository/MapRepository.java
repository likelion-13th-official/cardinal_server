package com.likelionsg13th.cardinal.common.repository;

import com.likelionsg13th.cardinal.common.domain.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Map,Long> {
}
