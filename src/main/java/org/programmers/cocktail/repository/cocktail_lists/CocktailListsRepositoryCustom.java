package org.programmers.cocktail.repository.cocktail_lists;

import org.programmers.cocktail.entity.CocktailLists;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CocktailListsRepositoryCustom extends JpaRepository<CocktailLists, Long> {

}
