package org.programmers.cocktail.repository.totalhitslog;

import java.time.LocalDateTime;
import java.util.Optional;
import org.programmers.cocktail.entity.TotalHitsLog;

public interface TotalHitsLogRepositoryCustom {
    Optional<TotalHitsLog> findByRecordedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

}
