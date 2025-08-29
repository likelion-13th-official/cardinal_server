package com.likelionsg13th.cardinal.users.repository;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import com.likelionsg13th.cardinal.users.domain.Stamp;
import com.likelionsg13th.cardinal.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StampRepository extends JpaRepository<Stamp,Long> {

    boolean existsByUserAndActivityType(Users user, ActivityType activityType);

    List<Stamp> findAllByUser(Users user);
}
