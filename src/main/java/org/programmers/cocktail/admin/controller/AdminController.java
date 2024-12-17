package org.programmers.cocktail.admin.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.service.DashboardService;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/admin")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

}
