package ru.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.service.model.RuleStatisticDTO;
import ru.service.model.entity.DynamicRule;
import ru.service.repository.RuleStatisticRepository;
import ru.service.service.DynamicRuleService;

import java.util.List;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {
    private final DynamicRuleService dynamicRuleService;
    private final RuleStatisticRepository ruleStatisticRepository;

    public DynamicRuleController(DynamicRuleService dynamicRuleService, RuleStatisticRepository ruleStatisticRepository) {
        this.dynamicRuleService = dynamicRuleService;
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    @PostMapping()
    public ResponseEntity<DynamicRule> createRule(@RequestBody DynamicRule dynamicRule) {
        dynamicRuleService.createRule(dynamicRule);
        return ResponseEntity.ok(dynamicRule);
    }

    @GetMapping
    public ResponseEntity<List<DynamicRule>> getAllRules() {
        final List<DynamicRule> rules = dynamicRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId) {
        dynamicRuleService.deleteRule(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<RuleStatisticDTO>> statistics() {
        return ResponseEntity.ok(ruleStatisticRepository.findAllStats());
    }

}
