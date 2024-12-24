package org.programmers.cocktail.repository.cocktails;

import jakarta.transaction.Transactional;
import java.util.List;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CocktailsRepository extends JpaRepository<Cocktails, Long>, CocktailsRepositoryCustom {

    List<Cocktails> findAllByOrderByLikesDesc();
    List<Cocktails> findByNameContaining(String userInput);

}

