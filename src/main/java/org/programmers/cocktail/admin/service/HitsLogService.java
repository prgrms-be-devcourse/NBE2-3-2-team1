package org.programmers.cocktail.admin.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.programmers.cocktail.repository.totalhitslog.TotalHitsLogRepository;
import org.springframework.stereotype.Service;

@Service
public class HitsLogService {

    private final TotalHitsLogRepository totalHitsLogRepository;


    public HitsLogService(TotalHitsLogRepository totalHitsLogRepository) {
        this.totalHitsLogRepository = totalHitsLogRepository;
    }

    public Optional<TotalHitsLog> getYesterdayLog() {
        LocalDateTime yesterdayStart = LocalDateTime.now().minusDays(1);
        LocalDateTime yesterdayEnd = yesterdayStart.plusHours(23).plusMinutes(59).plusSeconds(59);

        return totalHitsLogRepository.findByRecordedAtBetween(yesterdayStart, yesterdayEnd);
    }

}
