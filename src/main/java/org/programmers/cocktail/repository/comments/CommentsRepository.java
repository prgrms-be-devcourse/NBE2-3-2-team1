package org.programmers.cocktail.repository.comments;

import java.util.List;
import org.programmers.cocktail.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long>, CommentsRepositoryCustom {

//    List<Comments> findById(String author);
}
