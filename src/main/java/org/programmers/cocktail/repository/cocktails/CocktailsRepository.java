package org.programmers.cocktail.repository.cocktails;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CocktailsRepository extends JpaRepository<Cocktails, Long>, CocktailsRepositoryCustom {

    List<Cocktails> findAllByOrderByLikesDesc();
    List<Cocktails> findByNameContaining(String userInput);

    //CocktailsService.updateCocktailHits 메서드 Pessimistic Lock 적용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select c From cocktails c where c.id = :id")
    Optional<Cocktails> findByIdWithPessimisticLock(Long id);

}

