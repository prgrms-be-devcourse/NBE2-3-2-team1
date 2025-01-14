package org.programmers.cocktail.repository.cocktail_likes;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLikes;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CocktailLikesRepository extends JpaRepository<CocktailLikes, Long> {

    @Query(value = "select Count(clikes) from cocktails_likes clikes where clikes.cocktails.id= :cocktailId")
    Long countCocktailLikesById(Long cocktailId);

    @Query(value ="select clikes from cocktails_likes clikes where clikes.users.id = :userId and clikes.cocktails.id = :cocktailId")
    Optional<CocktailLikes> findByUserIdAndCocktailId(Long userId, Long cocktailId);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value ="Delete from cocktails_likes clikes where clikes.users.id = :userId and clikes.cocktails.id = :cocktailId")
    int deleteByUserIdAndCocktailId(Long userId, Long cocktailId);

    void deleteAllByUsers(Users users);

    @Query("SELECT cl FROM cocktails_likes cl WHERE cl.updatedAt > :lastSyncTime")
    List<CocktailLikes> findByUpdatedAtAfter(@Param("lastSyncTime") LocalDateTime lastSyncTime);

}
