package org.programmers.cocktail.admin.repository;


import java.util.List;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardCocktailRepository extends JpaRepository<Cocktails, Long> {

    List<Cocktails> findAllByOrderByLikesDesc();
      
}
