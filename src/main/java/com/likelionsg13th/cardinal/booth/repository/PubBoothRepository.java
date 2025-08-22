package com.likelionsg13th.cardinal.booth.repository;

import com.likelionsg13th.cardinal.booth.domain.subtype.PubBooth;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PubBoothRepository extends JpaRepository<PubBooth, Long> {




}
