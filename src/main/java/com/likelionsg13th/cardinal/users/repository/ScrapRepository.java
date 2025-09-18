package com.likelionsg13th.cardinal.users.repository;

import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.users.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap,Long> {
    List<Scrap> findAllByUser_IdAndContentIdInAndContentType(Long userId, List<Long> contentId, ContentType contentType);
    void deleteByUser_IdAndContentIdAndContentType(Long userId, Long contentId, ContentType contentType);
    boolean existsByUser_IdAndContentIdAndContentType(Long userId, Long contentId, ContentType contentType);
    boolean existsByIdAndUserId(Long scrapId, Long userId);
    boolean existsByUserIdAndContentIdAndContentType(Long userId, Long contentId, ContentType contentType);
    void deleteById(Long scrapId);
    void deleteAllByUserId(Long userId);

    List<Scrap> findAllByUser_Id(Long userId);
}
