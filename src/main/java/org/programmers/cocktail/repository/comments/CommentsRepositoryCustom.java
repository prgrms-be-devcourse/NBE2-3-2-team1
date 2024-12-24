package org.programmers.cocktail.repository.comments;

import java.time.LocalDateTime;
import java.util.List;
import org.programmers.cocktail.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentsRepositoryCustom {

    Long countTotalCommentsUntilYesterday(LocalDateTime today);

    List<Long> countCommentsList(LocalDateTime today);

    Page<Comments> searchByKeyword(String keyword, Pageable pageable);
}
