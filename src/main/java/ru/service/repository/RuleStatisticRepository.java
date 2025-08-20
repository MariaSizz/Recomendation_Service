package ru.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.model.RuleStatisticDTO;

import java.util.List;

@Repository
public class RuleStatisticRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void incrementCounter(String ruleId) {
        String sql = "INSERT INTO rule_statistics (rule_id, trigger_count) VALUES (?, 1) " +
                "ON CONFLICT (rule_id) DO UPDATE SET trigger_count = rule_statistics.trigger_count + 1";
        jdbcTemplate.update(sql, ruleId);
    }

    public List<RuleStatisticDTO> findAllStats() {
        String sql = "SELECT rule_id, trigger_count FROM rule_statistics";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RuleStatisticDTO(
                        rs.getString("rule_id"), // Изменено на getString
                        rs.getLong("trigger_count")
                ));
    }
}
