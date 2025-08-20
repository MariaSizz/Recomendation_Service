package ru.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.service.config.RuleCache;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final RuleCache ruleCache;
    private final BuildProperties buildProperties;

    @Autowired
    public ManagementController(RuleCache ruleCache, BuildProperties buildProperties) {
        this.ruleCache = ruleCache;
        this.buildProperties = buildProperties;
    }

    @PostMapping("/info")
    public ResponseEntity<Map<String, String>> getAppInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("Name", buildProperties.getName());
        map.put("Version", buildProperties.getVersion());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/clear-cashes")
    public ResponseEntity<String> clearCashes() {
        ruleCache.invalidateAll();
        return ResponseEntity.ok("Кеш успешно очищен");
    }

}
