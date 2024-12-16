package org.programmers.cocktail.admin.repository;

import org.programmers.cocktail.admin.dto.DashboardCocktailResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardCocktailRepository extends JpaRepository<DashboardCocktailResponse, Long> {

}
