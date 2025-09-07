package com.likelionsg13th.cardinal.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OneTimeCodeService {

    private final Map<String, Entry> store = new ConcurrentHashMap<>();

    @Value("${app.one-time-code.ttl-seconds:60}")
    private long ttlSeconds;

    public String issue(String subject) {
        cleanup();
        String code = UUID.randomUUID().toString().replace("-", "");
        store.put(code, new Entry(subject, Instant.now().plusSeconds(ttlSeconds)));
        return code;
    }

    /** 유효하면 subject 반환하고, 즉시 폐기(1회성) */
    public String consume(String code) {
        if (code == null) return null;
        Entry e = store.remove(code);
        if (e == null) return null;
        if (Instant.now().isAfter(e.expiresAt)) return null;
        return e.subject;
    }

    private void cleanup() {
        Instant now = Instant.now();
        store.entrySet().removeIf(en -> now.isAfter(en.getValue().expiresAt));
    }

    private record Entry(String subject, Instant expiresAt) {}
}
