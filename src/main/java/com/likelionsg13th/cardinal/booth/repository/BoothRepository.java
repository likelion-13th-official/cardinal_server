package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothRepository extends JpaRepository<Booth,Long> {
    List<Booth> findAllByCategoryAndLocation_Id(BoothCategory category, Long locationId);
    List<Booth> findAllByCategory(BoothCategory category);
    List<Booth> findByCategory(BoothCategory category);
    List<Booth> findAllByNameContaining(String name);
}
