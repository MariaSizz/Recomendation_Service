package ru.service.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.service.handler.RuleQueryHandler;

import java.util.List;
@Component
public class TransactionSumCompareQueryHandler implements RuleQueryHandler {
    @Autowired
    @Qualifier("recommendationsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean handle(String userId, List<String> arguments) {
        String productType = arguments.get(0);
        String transactionType = arguments.get(1);
        String comparisonOperator = arguments.get(2);
        int constant = Integer.parseInt(arguments.get(3));

        return compareTransactionSum(userId, productType, transactionType, comparisonOperator, constant);
    }

    private boolean compareTransactionSum(String userId, String productType,
                                          String transactionType, String comparisonOperator, int constant) {
        final String sql = "SELECT SUM(t.amount) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ? AND t.type = ?";

        try {
            Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType, transactionType);
            if (sum == null) sum = 0;

            switch (comparisonOperator) {
                case ">": return sum > constant;
                case "<": return sum < constant;
                case "=": return sum == constant;
                case ">=": return sum >= constant;
                case "<=": return sum <= constant;
                default: throw new IllegalArgumentException("Unknown comparison operator: " + comparisonOperator);
            }
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}