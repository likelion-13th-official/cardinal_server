package com.likelionsg13th.cardinal.pubOffice.repository;

import com.likelionsg13th.cardinal.pubOffice.domain.PubAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PubAdminRepository extends JpaRepository<PubAdmin, Long> {
    Optional<PubAdmin> findByAdminId(String adminId);
}
