package com.likelionsg13th.cardinal.auth.repository;

import com.likelionsg13th.cardinal.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{
    Optional<RefreshToken> findByToken(String refreshToken);
    void deleteByToken(String refreshToken);
    void deleteBySubject(String subject);
}
