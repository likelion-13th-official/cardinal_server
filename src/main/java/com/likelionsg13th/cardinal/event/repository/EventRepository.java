package com.likelionsg13th.cardinal.event.repository;

import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByNameContaining(String name);

}
