package org.programmers.cocktail.admin.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.dto.DashboardCocktailResponse;
import org.programmers.cocktail.admin.repository.DashboardCocktailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardService {

    @Autowired
    DashboardCocktailRepository dashboardRepository;


}
