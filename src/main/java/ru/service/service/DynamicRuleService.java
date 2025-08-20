package ru.service.service;

import org.springframework.stereotype.Service;
import ru.service.model.entity.DynamicRule;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.service.handler.RuleQueryHandler;
import ru.service.handler.impl.ActiveUserOfQueryHandler;
import ru.service.handler.impl.TransactionSumCompareDepositWithdrawQueryHandler;
import ru.service.handler.impl.TransactionSumCompareQueryHandler;
import ru.service.handler.impl.UserQueryHandler;
import ru.service.model.ProductDTO;
import ru.service.model.entity.RuleQuery;
import ru.service.repository.DynamicRuleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import ru.service.repository.RuleStatisticRepository;

@Service
public class DynamicRuleService {
    @Autowired
    private DynamicRuleRepository dynamicRuleRepository;

    @Autowired
    private UserQueryHandler userOfQueryHandler;

    @Autowired
    private ActiveUserOfQueryHandler activeUserOfQueryHandler;

    @Autowired
    private TransactionSumCompareQueryHandler transactionSumCompareQueryHandler;

    @Autowired
    private TransactionSumCompareDepositWithdrawQueryHandler transactionSumCompareDepositWithdrawQueryHandler;

    @Autowired
    private RuleStatisticRepository ruleStatisticRepository;

    public DynamicRule createRule(DynamicRule rule) {
        DynamicRule save = null;
        try {
            save = dynamicRuleRepository.save(rule);
        } catch (JsonProcessingException e) {
            return new DynamicRule();
        }
        return save;
    }

    public List<DynamicRule> getAllRules() {
        return dynamicRuleRepository.findAll();
    }

    public void deleteRule(String productId) {
        dynamicRuleRepository.delete(productId);
    }

    public List<ProductDTO> evaluateRules(String userName) {

        List<ProductDTO> recommendations = new ArrayList<>();
        List<DynamicRule> rules = dynamicRuleRepository.findAll();
        String idByUserName = dynamicRuleRepository.findIdByUserName(userName);

        for (DynamicRule rule : rules) {
            boolean result = true;
            for (RuleQuery query : rule.getRule()) {
                RuleQueryHandler handler = getHandler(query.getQuery());
                boolean queryResult = handler.handle(idByUserName, query.getArguments());
                if (query.isNegate()) {
                    queryResult = !queryResult;
                }
                result = result && queryResult;
            }
            if (result) {
                recommendations.add(new ProductDTO(rule.getProductName(), rule.getProductId(), rule.getProductText()));
            }
        }
        return recommendations;
    }

    public List<ProductDTO> getRecommendationsForUser(String username) {
        List<ProductDTO> productDTOs = evaluateRules(username);

        // 3. Инкрементим счётчики в транзакции
        productDTOs.forEach(rule -> {
            ruleStatisticRepository.incrementCounter(rule.getId());
        });

        return productDTOs;
    }


    private RuleQueryHandler getHandler(String queryType) {
        switch (queryType) {
            case "USER_OF":
                return userOfQueryHandler;
            case "ACTIVE_USER_OF":
                return activeUserOfQueryHandler;
            case "TRANSACTION_SUM_COMPARE":
                return transactionSumCompareQueryHandler;
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                return transactionSumCompareDepositWithdrawQueryHandler;
            default:
                throw new IllegalArgumentException("Unknown query type: " + queryType);
        }
    }
}
