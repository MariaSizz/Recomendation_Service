package ru.service.model;

public class RuleStatisticDTO {

    private String ruleId;
    private Long ruleName;

    public RuleStatisticDTO(String ruleId, Long ruleName) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRuleName() {
        return ruleName;
    }

    public void setRuleName(Long ruleName) {
        this.ruleName = ruleName;
    }
}
