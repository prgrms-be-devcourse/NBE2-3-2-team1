package org.programmers.cocktail.repository.comments;

import org.programmers.cocktail.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
