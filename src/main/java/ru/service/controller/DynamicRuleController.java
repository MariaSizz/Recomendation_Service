package ru.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.service.model.DynamicRule;
import ru.service.model.RuleQuery;
import ru.service.service.DynamicRuleService;

import java.util.List;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {
private final DynamicRuleService dynamicRuleService;

    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    @PostMapping()
    public ResponseEntity<DynamicRule> createRule(@RequestBody DynamicRule dynamicRule){
        dynamicRuleService.createRule(dynamicRule);
        return ResponseEntity.ok(dynamicRule);
    }

    @GetMapping
    public ResponseEntity<List<DynamicRule>> getAllRules(){
        final List<DynamicRule> rules = dynamicRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId ){
        dynamicRuleService.deleteRule(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, I>> statistics(){

    }

}
