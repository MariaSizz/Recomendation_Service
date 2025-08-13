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
                .expireAfterWrite(10, TimeUnit.MINUTES) // Кеш будет истекать через 10 минут
                .maximumSize(100) // Максимальное количество элементов в кеше
                .build();
    }

    public DynamicRule get(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, DynamicRule rule) {
        cache.put(key, rule);
    }

    public void invalidateAll(){
        cache.invalidateAll();
    }

    public void invalidate(String productId){
        cache.invalidate(productId);
    }
}

