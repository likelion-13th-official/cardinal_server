package com.likelionsg13th.cardinal.goods.repository;

import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Optional<Goods> findFirstByOrderByIdAsc();
    List<Goods> findAllByNameContaining(String name);

    Page<Goods> findByNameContaining(String query, Pageable pageable);
}
