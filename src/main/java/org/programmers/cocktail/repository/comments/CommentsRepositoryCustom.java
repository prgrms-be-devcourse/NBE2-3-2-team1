package org.programmers.cocktail.repository.comments;

import java.time.LocalDateTime;

public interface CommentsRepositoryCustom {

    Long countTotalCommentsUntilYesterday(LocalDateTime today);
}
