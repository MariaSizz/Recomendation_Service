package ru.service.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.config.RuleCache;
import ru.service.model.DynamicRule;
import ru.service.model.RuleQuery;

import java.util.List;

@Repository
public class DynamicRuleRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RuleCache ruleCache;

    public DynamicRule save(DynamicRule rule) throws JsonProcessingException {
        final String sql = "INSERT INTO dynamic_rules (product_name, product_id, product_text, rule, rule_count) VALUES (?, ?, ?, ?::jsonb, ?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{
                rule.getProductName(),
                rule.getProductId(),
                rule.getProductText(),
                objectMapper.writeValueAsString(rule.getRule()),
                rule.getRuleCount()
        }, Integer.class);
        rule.setId(id);
        ruleCache.put(rule.getProductId(), rule);
        return rule;
    }

    public List<DynamicRule> findAll() {
        final String sql = "SELECT * FROM dynamic_rules";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DynamicRule rule = new DynamicRule();
            rule.setId(rs.getInt("id"));
            rule.setProductName(rs.getString("product_name"));
            rule.setProductId(rs.getString("product_id"));
            rule.setProductText(rs.getString("product_text"));
            String ruleJson = rs.getString("rule");
            rule.setRuleCount(rs.getInt("rule_count"));
            List<RuleQuery> ruleList = null;
            try {
                ruleList = objectMapper.readValue(ruleJson, new TypeReference<List<RuleQuery>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            rule.setRule(ruleList);
            return rule;
        });
    }

    public void delete(String productId) {
        final String sql = "DELETE FROM dynamic_rules WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
        ruleCache.invalidate(productId);
    }

    public DynamicRule findByProductId(String productId) {
        DynamicRule cachedRule = ruleCache.get(productId);
        if (cachedRule != null) {
            return cachedRule;
        }

        final String sql = "SELECT * FROM dynamic_rules WHERE product_id = ?";
        DynamicRule rule = jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            DynamicRule dynamicRule = new DynamicRule();
            dynamicRule.setId(rs.getInt("id"));
            dynamicRule.setProductName(rs.getString("product_name"));
            dynamicRule.setProductId(rs.getString("product_id"));
            dynamicRule.setProductText(rs.getString("product_text"));
            String ruleJson = rs.getString("rule");
            List<RuleQuery> ruleList = null;
            try {
                ruleList = objectMapper.readValue(ruleJson, new TypeReference<List<RuleQuery>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            dynamicRule.setRule(ruleList);
            return dynamicRule;
        });

        if (rule != null) {
            ruleCache.put(productId, rule); // Кешируем правило
        }
        return rule;
    }

    public boolean getRecommendationsForUser(DynamicRule dynamicRule){
        final Integer ruleCount = dynamicRule.getRuleCount();
        final String productName = dynamicRule.getProductName();
        final String productId = dynamicRule.getProductId();
        final String productText = dynamicRule.getProductText();
        final List<RuleQuery> rule = dynamicRule.getRule();
        final Integer id = dynamicRule.getId();
        final String sql = ""
    }
}
