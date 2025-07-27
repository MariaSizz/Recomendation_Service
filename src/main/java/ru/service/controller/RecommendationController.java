package ru.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.service.model.UsersRecommendationsDTO;
import ru.service.service.RecommendationService;

import java.util.UUID;

@RequestMapping("/recommendation")
@RestController
public class RecommendationController {
    private final RecommendationService service;

    @Autowired
    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public UsersRecommendationsDTO retrieveRecommendationsForUser(@PathVariable("userId") UUID userId) {
        return service.retrieveRecommendationsForUser(userId);
    }
}
