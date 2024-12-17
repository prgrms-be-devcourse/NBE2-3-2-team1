package org.programmers.cocktail.search.repository;

import java.util.List;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CocktailsJpaRepository extends JpaRepository<Cocktails, Long> {
    List<Cocktails> findByNameContaining(String userInput);
}
