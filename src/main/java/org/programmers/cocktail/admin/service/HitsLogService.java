package org.programmers.cocktail.admin.service;

import java.time.LocalDateTime;
import java.util.List;
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

    public long getYesterdayLog() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);

        return totalHitsLogRepository.getYesterdayLog(yesterday);
    }

    public long getTodayLog() {
        LocalDateTime today = LocalDateTime.now();

        return totalHitsLogRepository.getTodayLog(today);
    }

    public List<Long> getListLog() {
        LocalDateTime today = LocalDateTime.now();

        return totalHitsLogRepository.getListLog(today);
    }

}
