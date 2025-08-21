package com.likelionsg13th.cardinal.event.repository;

import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByNameContaining(String name);

}
