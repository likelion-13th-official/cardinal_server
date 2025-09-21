package com.likelionsg13th.cardinal.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneTimeCodeService {

    private final StringRedisTemplate redis;

    @Value("${app.one-time-code.ttl-seconds:60}")
    private long ttlSeconds;

    private static final String KEY_PREFIX = "one-time-code:";

    public String issue(String subject) {
        String code = UUID.randomUUID().toString().replace("-", "");
        String key = KEY_PREFIX + code;
        redis.opsForValue().set(key, subject, ttlSeconds, TimeUnit.SECONDS);

        String issueMsg = String.format("[OneTimeCode] Issued code for subject '%s'. Code: %s", subject, code);
        log.info(issueMsg);
        System.out.println("\u001B[34m" + issueMsg + "\u001B[0m"); // 파란색으로 출력

        return code;
    }

    /** 유효하면 subject 반환하고, 즉시 폐기(1회성) */
    public String consume(String code) {
        if (code == null) return null;
        String key = KEY_PREFIX + code;

        String attemptMsg = String.format("[OneTimeCode] Attempting to consume code: %s", code);
        log.info(attemptMsg);
        System.out.println(attemptMsg);

        String subject = redis.opsForValue().getAndDelete(key);

        if (subject != null) {
            String successMsg = String.format("[OneTimeCode] Successfully consumed code for subject: '%s'", subject);
            log.info(successMsg);
            System.out.println("\u001B[32m" + successMsg + "\u001B[0m"); // 초록색으로 출력
        } else {
            String failMsg = String.format("[OneTimeCode] Failed to consume code. It might be invalid, expired, or already used. Code: %s", code);
            log.warn(failMsg);
            System.out.println("\u001B[31m" + failMsg + "\u001B[0m"); // 빨간색으로 출력
        }

        return subject;
    }
}