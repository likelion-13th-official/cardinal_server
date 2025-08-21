package com.likelionsg13th.cardinal.users.repository;

import com.likelionsg13th.cardinal.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByProviderAndProviderId(String provider, String providerId);
}
