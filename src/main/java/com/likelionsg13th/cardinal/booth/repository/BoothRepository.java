package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoothRepository extends JpaRepository<Booth,Long> {
    List<Booth> findByCategory(BoothCategory category);
}
