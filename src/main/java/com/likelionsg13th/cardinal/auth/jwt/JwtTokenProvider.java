package com.likelionsg13th.cardinal.auth.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessValidityMs;
    private final long refreshValidityMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-seconds}") long accessSec,
            @Value("${jwt.refresh-token-validity-seconds}") long refreshSec
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessValidityMs = accessSec * 1000;
        this.refreshValidityMs = refreshSec * 1000;
    }

    public String createAccessToken(String subject) {
        return createToken(subject, Map.of(), accessValidityMs);
    }
    public String createRefreshToken(String subject) {
        return createToken(subject, Map.of("typ","refresh"), refreshValidityMs);
    }

    private String createToken(String subject, Map<String,Object> claims, long validityMs) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)                     // "kakao:{id}"
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public String getSubject(String token) {
        return parse(token).getBody().getSubject();
    }
    public boolean isRefreshToken(String token) {
        Object typ = parse(token).getBody().get("typ"); return "refresh".equals(typ);
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(stripBearer(token));
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    private String stripBearer(String token) {
        if (token == null) return null;
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}