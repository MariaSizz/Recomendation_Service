package ru.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Отправляет пользователю рекомендованные продукты",
            description = "Метод фильтрует рекомендации по конкретному пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рекомендации, отправленные пользователю"),
            @ApiResponse(responseCode = "400", description = "Неправильные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера"),
    })
    @GetMapping("/{userId}")
    public UsersRecommendationsDTO retrieveRecommendationsForUser(@PathVariable("userId") UUID userId) {
        return service.retrieveRecommendationsForUser(userId);
    }

}
