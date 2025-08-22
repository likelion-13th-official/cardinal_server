package com.likelionsg13th.cardinal.event.repository;

import com.likelionsg13th.cardinal.map.dto.MapSearchDto;
import com.likelionsg13th.cardinal.event.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT new com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto(" +
            "'EVENT',"+
            "g.name," +
            "g.id," +
            "g.location.position," +
            "g.location.longitude," +
            "g.location.latitude) " +
            "FROM Event g WHERE g.name LIKE :keyword")
    List<MapSearchDto> findAllByNameContaining(@Param("keyword") String keyword);

    Page<Event> findByNameContaining(String query, Pageable pageable);
}
