package org.programmers.cocktail.repository.cocktail_likes;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CocktailLikesRepositoryCustom extends JpaRepository<CocktailLikes, Long> {

    @Query(value ="select clikes from cocktails_likes clikes where clikes.users.id = :userId and clikes.cocktails.id = :cocktailId")
    Optional<CocktailLikes> findByUserIdAndCocktailId(Long userId, Long cocktailId);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value ="Delete from cocktails_likes clikes where clikes.users.id = :userId and clikes.cocktails.id = :cocktailId")
    int deleteByUserIdAndCocktailId(Long userId, Long cocktailId);
}
