package org.programmers.cocktail.repository.cocktail_likes;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CocktailLikesRepositoryCustom extends JpaRepository<CocktailLikes, Long> {

    @Query(value ="select cl from cocktails_likes cl where cl.users.id = :userId and cl.cocktails.id = :cocktailId")
    Optional<CocktailLikes> findByUserIdAndCocktailId(Long userId, Long cocktailId);
}
