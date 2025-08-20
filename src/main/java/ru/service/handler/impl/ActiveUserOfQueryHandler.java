package ru.service.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.service.handler.RuleQueryHandler;

import java.util.List;

@Component
public class ActiveUserOfQueryHandler implements RuleQueryHandler {
    @Autowired
    @Qualifier("recommendationsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean handle(String userId, List<String> arguments) {
        String productType = arguments.get(0);
        return checkActiveUserOfProduct(userId, productType);
    }

    private boolean checkActiveUserOfProduct(String userId, String productType) {
        final String sql = "SELECT COUNT(*) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ? " +
                "GROUP BY t.user_id HAVING COUNT(*) >= 5";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
