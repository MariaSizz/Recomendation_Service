package ru.service.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.service.handler.RuleQueryHandler;

import java.util.List;
@Component
public class ActiveUserOfQueryHandler implements RuleQueryHandler {
    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Override
    public boolean handle(String userId, List<String> arguments) {
        String productType = arguments.get(0);
        return checkActiveUserOfProduct(userId, productType);
    }
    private boolean checkActiveUserOfProduct(String userId, String productType) {
        final String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND product_type = ? GROUP BY user_id HAVING COUNT(*) >= 5";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, productType}, Integer.class);
        return count != null && count > 0;
    }
}
