package org.programmers.cocktail.repository.comments;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentsRepositoryCustom {

    Long countTotalCommentsUntilYesterday(LocalDateTime today);

    List<Long> countCommentsList(LocalDateTime today);
}
