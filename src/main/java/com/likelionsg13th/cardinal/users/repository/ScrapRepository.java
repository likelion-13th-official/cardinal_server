package com.likelionsg13th.cardinal.users.repository;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap,Long> {

    boolean existsByUser_IdAndContentIdAndContentType(Long userId, Long contentId, ContentType contentType);
}
