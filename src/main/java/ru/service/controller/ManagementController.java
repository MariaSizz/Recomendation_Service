package ru.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.service.config.RuleCache;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

   private final RuleCache ruleCache;

    public ManagementController(RuleCache ruleCache) {
        this.ruleCache = ruleCache;
    }

    public ResponseEntity<Map<String, String>> getRuleStatistics(){
        return;
    }

    public ResponseEntity<Void> clearCashes(){
        ruleCache.invalidate();
    }
}
