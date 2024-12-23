package org.programmers.cocktail.repository.totalhitslog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.TotalHitsLog;

public interface TotalHitsLogRepositoryCustom {
    long getYesterdayLog(LocalDateTime yesterday);

    long getTodayLog(LocalDateTime today);

    List<Long> getListLog(LocalDateTime today);

}
