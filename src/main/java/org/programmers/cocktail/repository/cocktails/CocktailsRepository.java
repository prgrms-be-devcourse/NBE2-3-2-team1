package org.programmers.cocktail.repository.cocktails;

import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CocktailsRepository extends JpaRepository<Cocktails, Long> {

    interface CocktailListsRepository extends JpaRepository<CocktailLists, Long> {
    }
}
