package ru.service.service;

import org.springframework.stereotype.Service;
import ru.service.model.ProductDTO;
import ru.service.model.UsersRecommendationsDTO;
import ru.service.repository.RecommendationsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.service.constant.ProductsConstants.*;

@Service
public class RecommendationService {
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public UsersRecommendationsDTO retrieveRecommendationsForUser(UUID id) {
        Map<String, Boolean> ruleResults = recommendationsRepository.checkAllRulesForUser(id);
        List<ProductDTO> recommendations = new ArrayList<>();

        if (ruleResults.get("INVEST_500_RULE_1") &&
                ruleResults.get("INVEST_500_RULE_2") &&
                ruleResults.get("INVEST_500_RULE_3")) {
            recommendations.add(new ProductDTO(FIRST_CONDITION_PRODUCT_NAME, FIRST_CONDITION_PRODUCT_ID, FIRST_CONDITION_TEXT));
        }

        if (ruleResults.get("TOP_SAVING_RULE_1") &&
                ruleResults.get("TOP_SAVING_RULE_2") &&
                ruleResults.get("TOP_SAVING_RULE_3")) {
            recommendations.add(new ProductDTO(SECOND_CONDITION_PRODUCT_NAME, SECOND_CONDITION_PRODUCT_ID, SECOND_CONDITION_TEXT));
        }

        if (ruleResults.get("CREDIT_RULE_1") &&
                ruleResults.get("CREDIT_RULE_2") &&
                ruleResults.get("CREDIT_RULE_3")) {
            recommendations.add(new ProductDTO(THIRD_CONDITION_PRODUCT_NAME, THIRD_CONDITION_PRODUCT_ID, THIRD_CONDITION_TEXT));
        }
        return new UsersRecommendationsDTO(id, recommendations);
    }
}
