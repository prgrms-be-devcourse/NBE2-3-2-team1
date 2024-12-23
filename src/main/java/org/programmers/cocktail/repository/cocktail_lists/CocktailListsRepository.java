package org.programmers.cocktail.repository.cocktail_lists;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CocktailListsRepository extends JpaRepository<CocktailLists, Long>{
    @Query(value ="select clist from cocktail_lists clist where clist.users.id = :userId and clist.cocktails.id = :cocktailId")
    Optional<CocktailLists> findByUserIdAndCocktailId(Long userId, Long cocktailId);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value ="Delete from cocktail_lists clist where clist.users.id = :userId and clist.cocktails.id = :cocktailId")
    int deleteByUserIdAndCocktailId(Long userId, Long cocktailId);
}
