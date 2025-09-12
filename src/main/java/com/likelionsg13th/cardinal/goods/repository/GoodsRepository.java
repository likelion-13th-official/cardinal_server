package com.likelionsg13th.cardinal.goods.repository;

import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.map.domain.Map;
import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Optional<Goods> findFirstByOrderByIdAsc();

    List<Goods> findAllByNameLike(@Param("keyword") String keyword);


    @Query("SELECT p.location" +
            " FROM Performance p " +
            " WHERE p.id = 1")
    Map findLocationFirstById();


}
