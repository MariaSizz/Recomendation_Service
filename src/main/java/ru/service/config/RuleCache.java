package ru.service.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;
import ru.service.model.entity.DynamicRule;

import java.util.Objects;
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
        Objects.requireNonNull(key, "Cache key cannot be null");
        Objects.requireNonNull(rule, "Cache value cannot be null");
        cache.put(key, rule);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public void invalidate(String productId) {
        cache.invalidate(productId);
    }
}

