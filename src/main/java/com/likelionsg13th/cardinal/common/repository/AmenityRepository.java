package com.likelionsg13th.cardinal.common.repository;

import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity,Long> {

    List<Amenity> findAllByNameContaining(String name);
}
