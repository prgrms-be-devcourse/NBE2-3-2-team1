package org.programmers.cocktail.repository.cocktail_lists;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CocktailListsRepositoryCustom extends JpaRepository<CocktailLists, Long> {
    @Query(value ="select cocklist from cocktail_lists cocklist where cocklist.users.id = :userId and cocklist.cocktails.id = :cocktailId")
    Optional<CocktailLists> findByUserIdAndCocktailId(Long userId, Long cocktailId);
}
