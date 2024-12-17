package org.programmers.cocktail.repository.cocktails;

import java.util.List;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CocktailsRepository extends JpaRepository<Cocktails, Long> {

    List<Cocktails> findAllByOrderByLikesDesc();
}
