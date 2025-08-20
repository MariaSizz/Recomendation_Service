package ru.service.model;

import java.util.List;
import java.util.UUID;

public class UsersRecommendationsDTO {
    private UUID userId;
    private List<ProductDTO> recommendations;

    public UsersRecommendationsDTO() {
    }

    public UsersRecommendationsDTO(UUID userId, List<ProductDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setRecommendations(List<ProductDTO> recommendations) {
        this.recommendations = recommendations;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<ProductDTO> getRecommendations() {
        return recommendations;
    }
}
