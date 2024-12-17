package org.programmers.cocktail.admin.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.repository.DashboardCocktailRepository;
import org.programmers.cocktail.admin.repository.DashboardUserRepository;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardService {

    private final DashboardCocktailRepository dashboardCocktailRepository;

    private final DashboardUserRepository dashboardUserRepository;

    @Autowired
    public DashboardService(DashboardCocktailRepository dashboardCocktailRepository,
        DashboardUserRepository dashboardUserRepository) {
        this.dashboardCocktailRepository = dashboardCocktailRepository;
        this.dashboardUserRepository = dashboardUserRepository;
    }

    public List<Cocktails> getCocktailsByLikesDesc() {
        return dashboardCocktailRepository.findAllByOrderByLikesDesc();
    }

    public int getUserCount() {
        return dashboardUserRepository.findAll().size();
    }

}
