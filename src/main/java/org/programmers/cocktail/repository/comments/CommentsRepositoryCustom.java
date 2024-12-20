package org.programmers.cocktail.repository.comments;

import java.time.LocalDateTime;
import java.util.List;
import org.programmers.cocktail.entity.Comments;
import org.springframework.data.jpa.repository.Query;

public interface CommentsRepositoryCustom {

    Long countTotalCommentsUntilYesterday(LocalDateTime today);


}

