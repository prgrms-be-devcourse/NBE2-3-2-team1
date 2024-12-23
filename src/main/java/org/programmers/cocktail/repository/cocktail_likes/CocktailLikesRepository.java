package org.programmers.cocktail.repository.cocktail_likes;

import org.programmers.cocktail.entity.CocktailLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CocktailLikesRepository extends JpaRepository<CocktailLikes, Long> {

    @Query(value = "select Count(clikes) from cocktails_likes clikes where clikes.cocktails.id= :cocktailId")
    Long countCocktailLikesById(Long cocktailId);
}
