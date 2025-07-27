package ru.service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.model.ProductDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.service.constant.ProductsConstants.*;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getRandomTransactionAmount(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t LIMIT 1",
                Integer.class);
        return result != null ? result : 0;
    }

    public boolean userHasProductType(UUID userId, String productType) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType));
    }

    public boolean userHasNoProductType(UUID userId, String productType) {
        return !userHasProductType(userId, productType);
    }

    public int getTotalDepositsByProductType(UUID userId, String productType) {
        String sql = """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

    public int getTotalSpendingByProductType(UUID userId, String productType) {
        String sql = """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

    public int getTotalSavingsDeposits(UUID userId) {
        String sql = """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = 'SAVING' AND t.type = 'DEPOSIT'
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    public Map<String, Boolean> checkAllRulesForUser(UUID userId) {
        Map<String, Boolean> ruleResults = new HashMap<>();

        ruleResults.put("INVEST_500_RULE_1", userHasProductType(userId, DEBIT));
        ruleResults.put("INVEST_500_RULE_2", userHasNoProductType(userId, INVEST));
        ruleResults.put("INVEST_500_RULE_3", getTotalSavingsDeposits(userId) > 1000);

        ruleResults.put("TOP_SAVING_RULE_1", userHasProductType(userId, DEBIT));

        int totalDebitDeposits = getTotalDepositsByProductType(userId, DEBIT);
        int totalSavingDeposits = getTotalDepositsByProductType(userId, SAVING);
        int totalDebitSpending = getTotalSpendingByProductType(userId, DEBIT);

        ruleResults.put("TOP_SAVING_RULE_2", totalDebitDeposits >= 50000 || totalSavingDeposits >= 50000);
        ruleResults.put("TOP_SAVING_RULE_3", totalDebitDeposits > totalDebitSpending);

        ruleResults.put("CREDIT_RULE_1", userHasNoProductType(userId, CREDIT));
        ruleResults.put("CREDIT_RULE_2", totalDebitDeposits > totalDebitSpending);
        ruleResults.put("CREDIT_RULE_3", totalDebitSpending > 100000);
        return ruleResults;
    }

}
