package ru.service.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.service.handler.RuleQueryHandler;

import java.util.List;
import java.util.Map;

@Component
public class TransactionSumCompareDepositWithdrawQueryHandler implements RuleQueryHandler {
    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Override
    public boolean handle(String userId, List<String> arguments) {
        String productType = arguments.get(0);
        String comparisonOperator = arguments.get(1);
        return compareDepositWithdraw(userId, productType, comparisonOperator);
    }
    private boolean compareDepositWithdraw(String userId, String productType, String comparisonOperator) {
        final String sql = "SELECT " +
                "SUM(CASE WHEN transaction_type = 'DEPOSIT' THEN amount ELSE 0 END) AS deposit_sum, " +
                "SUM(CASE WHEN transaction_type = 'WITHDRAW' THEN amount ELSE 0 END) AS withdraw_sum " +
                "FROM transactions WHERE user_id = ? AND product_type = ?";

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, new Object[]{userId, productType});
        Integer depositSum = (Integer) result.get("deposit_sum");
        Integer withdrawSum = (Integer) result.get("withdraw_sum");
        if (depositSum == null) depositSum = 0;
        if (withdrawSum == null) withdrawSum = 0;
        switch (comparisonOperator) {
            case ">":
                return depositSum > withdrawSum;
            case "<":
                return depositSum < withdrawSum;
            case "=":
                return depositSum.equals(withdrawSum);
            case ">=":
                return depositSum >= withdrawSum;
            case "<=":
                return depositSum <= withdrawSum;
            default:
                throw new IllegalArgumentException("Unknown comparison operator: " + comparisonOperator);
        }
    }
}
