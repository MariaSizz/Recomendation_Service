package ru.service.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;
import ru.service.model.DynamicRule;

import java.util.concurrent.TimeUnit;

@Component
public class RuleCache {
    private final Cache<String, DynamicRule> cache;

    public RuleCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    public DynamicRule get(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, DynamicRule rule) {
        cache.put(key, rule);
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }
}

