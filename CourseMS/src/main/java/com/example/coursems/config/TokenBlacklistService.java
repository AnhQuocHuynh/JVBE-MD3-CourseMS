package com.example.coursems.config;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklist(String token, long expiresAtMillis) {
        cleanupExpired();
        blacklistedTokens.put(token, expiresAtMillis);
    }

    public boolean isBlacklisted(String token) {
        cleanupExpired();
        return blacklistedTokens.containsKey(token);
    }

    private void cleanupExpired() {
        long now = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() <= now);
    }
}
