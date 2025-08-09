package ru.service.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.service.handler.RuleQueryHandler;

import java.util.List;

@Component
public class UserQueryHandler implements RuleQueryHandler {
    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Override
    public boolean handle(String userId, List<String> arguments) {
        String productType = arguments.get(0);
        return checkUserOfProduct(userId, productType);
    }
    private boolean checkUserOfProduct(String userId, String productType) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND product_type = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, productType}, Integer.class);
        return count != null && count > 0;
    }
}

