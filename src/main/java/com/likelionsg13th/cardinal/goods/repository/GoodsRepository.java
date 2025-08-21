package com.likelionsg13th.cardinal.goods.repository;

import com.likelionsg13th.cardinal.goods.domain.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Optional<Goods> findFirstByOrderByIdAsc();
}
