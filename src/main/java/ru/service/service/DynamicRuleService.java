package ru.service.service;

import org.springframework.stereotype.Service;
import ru.service.model.DynamicRule;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.service.handler.RuleQueryHandler;
import ru.service.handler.impl.ActiveUserOfQueryHandler;
import ru.service.handler.impl.TransactionSumCompareDepositWithdrawQueryHandler;
import ru.service.handler.impl.TransactionSumCompareQueryHandler;
import ru.service.handler.impl.UserQueryHandler;
import ru.service.model.RuleQuery;
import ru.service.repository.DynamicRuleRepository;

import org.springframework.beans.factory.annotation.Autowired;

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

    public DynamicRule createRule(DynamicRule rule){
        DynamicRule save = null;
        try {
            save = dynamicRuleRepository.save(rule);
        }catch (JsonProcessingException e){
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

    public boolean evaluateRules(String userId) {
        List<DynamicRule> rules = dynamicRuleRepository.findAll();
        for (DynamicRule rule : rules) {
            boolean result = true;
            for (RuleQuery query : rule.getRule()) {
                RuleQueryHandler handler = getHandler(query.getQuery());
                boolean queryResult = handler.handle(userId, query.getArguments());
                if (query.isNegate()) {
                    queryResult = !queryResult;
                }
                result = result && queryResult;
            }
            if (result) {
                //добавить рекомендацию
            }
        }
        return false; // или true, если есть рекомендации
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
