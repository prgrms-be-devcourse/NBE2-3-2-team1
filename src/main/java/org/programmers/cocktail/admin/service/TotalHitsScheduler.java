package org.programmers.cocktail.admin.service;

import java.time.LocalDateTime;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.programmers.cocktail.repository.totalhitslog.TotalHitsLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TotalHitsScheduler {

    private final CocktailsRepository cocktailsRepository;
    private final TotalHitsLogRepository totalHitsLogRepository;


    public TotalHitsScheduler(CocktailsRepository cocktailsRepository,
        TotalHitsLogRepository totalHitsLogRepository) {
        this.cocktailsRepository = cocktailsRepository;
        this.totalHitsLogRepository = totalHitsLogRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void recordTotalHits() {
        Long totalHits = cocktailsRepository.getTotalHits();
        TotalHitsLog log = new TotalHitsLog(totalHits, LocalDateTime.now());

        totalHitsLogRepository.save(log);
    }
}
