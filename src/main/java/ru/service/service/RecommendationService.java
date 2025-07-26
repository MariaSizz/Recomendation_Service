package ru.service.service;

import org.springframework.stereotype.Service;
import ru.service.model.UsersRecommendationsDTO;
import ru.service.repository.RecommendationsRepository;

import java.util.UUID;

@Service
public class RecommendationService {
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public UsersRecommendationsDTO retrieveRecommendationsForUser(UUID id) {
        final int randomTransactionAmount = recommendationsRepository.getRandomTransactionAmount(id);
        System.out.println(randomTransactionAmount);
        UsersRecommendationsDTO recommendations = new UsersRecommendationsDTO();
        recommendations.setUserId(UUID.randomUUID());
        return recommendations;
    }
}
