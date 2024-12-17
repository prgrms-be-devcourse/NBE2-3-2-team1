package org.programmers.cocktail.admin.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.repository.DashboardRepository;
import org.programmers.cocktail.admin.repository.DashboardUserRepository;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    private final DashboardUserRepository dashboardUserRepository;

    @Autowired
    public DashboardService(DashboardRepository dashboardRepository,
        DashboardUserRepository dashboardUserRepository) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardUserRepository = dashboardUserRepository;
    }

    public List<Cocktails> getCocktailsByLikesDesc() {
        return dashboardRepository.findAllByOrderByLikesDesc();
    }

    public int getUserCount() {
        return dashboardUserRepository.findAll().size();
    }

    public Optional<Users> getUserById(Long id) {
        return dashboardUserRepository.findById(id);
    }

}
