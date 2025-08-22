package com.likelionsg13th.cardinal.goods.repository;

import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapLabelDto;
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

    @Query("SELECT new com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapLabelDto(" +
            "'GOODS',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM Goods g WHERE g.name LIKE :keyword")
    List<MapLabelDto> findAllByNameContaining(@Param("keyword") String keyword);

    Page<Goods> findByNameContaining(String query, Pageable pageable);
}
