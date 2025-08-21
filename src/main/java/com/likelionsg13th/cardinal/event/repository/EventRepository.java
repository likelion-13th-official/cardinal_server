package com.likelionsg13th.cardinal.event.repository;

import com.likelionsg13th.cardinal.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
}
