package com.likelionsg13th.cardinal.users.repository;

import com.likelionsg13th.cardinal.users.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap,Long> {

    boolean deleteByUserId(Long id);
}
