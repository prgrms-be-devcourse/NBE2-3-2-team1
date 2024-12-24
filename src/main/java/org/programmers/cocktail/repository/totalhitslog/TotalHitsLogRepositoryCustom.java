package org.programmers.cocktail.repository.totalhitslog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TotalHitsLogRepositoryCustom {
    long getYesterdayLog(LocalDateTime yesterday);

    long getTodayLog(LocalDateTime today);

    List<Long> getListLog(LocalDateTime today);



}
