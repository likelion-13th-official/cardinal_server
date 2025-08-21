package com.likelionsg13th.cardinal.users.repository;

import com.likelionsg13th.cardinal.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByProviderAndProviderId(String provider, String providerId);
}