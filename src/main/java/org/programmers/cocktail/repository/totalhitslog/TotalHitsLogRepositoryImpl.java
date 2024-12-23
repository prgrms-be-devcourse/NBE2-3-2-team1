package org.programmers.cocktail.repository.totalhitslog;

import static org.programmers.cocktail.entity.QTotalHitsLog.totalHitsLog;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.springframework.stereotype.Repository;

@Repository
public class TotalHitsLogRepositoryImpl implements TotalHitsLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TotalHitsLogRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<TotalHitsLog> findByRecordedAtBetween(LocalDateTime startOfDay,
        LocalDateTime endOfDay) {
        TotalHitsLog result = queryFactory
            .selectFrom(totalHitsLog)
            .where(
                totalHitsLog.recordedAt.between(startOfDay,endOfDay)
            )
            .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<TotalHitsLog> findSevendaysByRecordedAtBetween(LocalDateTime startOfDay,
        LocalDateTime endOfDay) {
        return List.of();
    }
}
