package ru.service.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.service.handler.RuleQueryHandler;

import java.util.List;
import java.util.Map;
@Component
public class TransactionSumCompareDepositWithdrawQueryHandler implements RuleQueryHandler {
    @Autowired
    @Qualifier("recommendationsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean handle(String userId, List<String> arguments) {
        String productType = arguments.get(0);
        String comparisonOperator = arguments.get(1);
        return compareDepositWithdraw(userId, productType, comparisonOperator);
    }

    private boolean compareDepositWithdraw(String userId, String productType, String comparisonOperator) {
        final String sql = "SELECT " +
                "SUM(CASE WHEN t.type = 'DEPOSIT' THEN t.amount ELSE 0 END) AS deposit_sum, " +
                "SUM(CASE WHEN t.type = 'WITHDRAW' THEN t.amount ELSE 0 END) AS withdraw_sum " +
                "FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE t.user_id = ? AND p.type = ?";

        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, userId, productType);
            Integer depositSum = result.get("deposit_sum") != null ? ((Number)result.get("deposit_sum")).intValue() : 0;
            Integer withdrawSum = result.get("withdraw_sum") != null ? ((Number)result.get("withdraw_sum")).intValue() : 0;

            switch (comparisonOperator) {
                case ">": return depositSum > withdrawSum;
                case "<": return depositSum < withdrawSum;
                case "=": return depositSum.equals(withdrawSum);
                case ">=": return depositSum >= withdrawSum;
                case "<=": return depositSum <= withdrawSum;
                default: throw new IllegalArgumentException("Unknown comparison operator: " + comparisonOperator);
            }
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}