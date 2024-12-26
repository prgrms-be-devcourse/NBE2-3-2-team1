package org.programmers.cocktail.repository.comments;

import jakarta.transaction.Transactional;
import java.util.List;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentsRepository extends JpaRepository<Comments, Long>, CommentsRepositoryCustom {

    void deleteById(Long id);

    @Query(value ="select cmt from comments cmt where cmt.cocktails.id = :cocktailId")
    List<Comments> findByCocktailId(Long cocktailId);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("Delete from comments cmt where cmt.id = :commentId")
    int deleteByIdWithReturnAffectedRowCount(Long commentId);

    void deleteAllByUsers(Users users);
}
