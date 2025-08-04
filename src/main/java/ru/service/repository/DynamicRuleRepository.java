package ru.service.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.service.model.DynamicRule;
import ru.service.model.RuleQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DynamicRuleRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    

    public DynamicRuleRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void addDynamicRule(DynamicRule dynamicRule) {
        final String sql = "INSERT INTO dynamic_rules (product_name, product_id, product_text, rule) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, dynamicRule.getProductName(), dynamicRule.getProductId(), dynamicRule.getProductText(), dynamicRule.getRule());
    }

    public List<DynamicRule> getAllRules() {
        final String sql = "SELECT * FROM dynamic_rules";
        jdbcTemplate.query(sql, new RowMapper<DynamicRule>() {
            @Override
            public DynamicRule mapRow(ResultSet rs, int rowNum) throws SQLException {
                DynamicRule dynamicRule = new DynamicRule();
                dynamicRule.setId(rs.getInt("id"));
                dynamicRule.setProductName(rs.getString("product_name"));
                dynamicRule.setProductId(rs.getString("product_id"));
                dynamicRule.setProductText(rs.getString("product_text"));
                final String ruleJson = rs.getString("rule");
                try {
                    final List<RuleQuery> ruleQueries = objectMapper.readValue(ruleJson, new TypeReference<List<RuleQuery>>() {});
                    dynamicRule.setRule(ruleQueries);
                } catch (JsonProcessingException e) {
                    throw new InternalError("Ошибка парсинга Json");
                }
                return dynamicRule;
            }
        });
        return new ArrayList<>();
    }
    public void deleteRule(String productId){
        final String sql = "DELETE FROM dynamic_rules WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
