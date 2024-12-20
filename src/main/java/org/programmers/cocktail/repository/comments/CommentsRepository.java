package org.programmers.cocktail.repository.comments;

import java.util.List;
import org.programmers.cocktail.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentsRepository extends JpaRepository<Comments, Long>, CommentsRepositoryCustom {
    @Query(value ="select cmt from comments cmt where cmt.cocktails.id = :cocktailId")
    List<Comments> findByCocktailId(Long cocktailId);
}
