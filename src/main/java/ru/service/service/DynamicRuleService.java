package ru.service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;
import ru.service.model.DynamicRule;
import ru.service.repository.DynamicRuleRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DynamicRuleService {
private final DynamicRuleRepository repository;
private final Cache<String, List<DynamicRule>> cache;

    public DynamicRuleService(DynamicRuleRepository repository) {
        this.repository = repository;
            this.cache = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(100).build();
    }
    public void createRule(DynamicRule dynamicRule){
        repository.addDynamicRule(dynamicRule);
        cache.invalidateAll();
    }
    public List<DynamicRule> getRules(){
        return repository.getAllRules();
    }
    public void removeRule(String productId){
        repository.deleteRule(productId);
        cache.invalidateAll();
    }
    public List<DynamicRule> getRulesForUser(String userId){
        final List<DynamicRule> cachedRules = cache.getIfPresent(userId);
        if (cachedRules != null) {
            return cachedRules;
        }
        final List<DynamicRule> allRules = repository.getAllRules();
        cache.put(userId, allRules);
        return allRules;
    }
}
