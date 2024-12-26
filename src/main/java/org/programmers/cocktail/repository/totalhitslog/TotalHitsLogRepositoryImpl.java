package org.programmers.cocktail.repository.totalhitslog;

import static org.programmers.cocktail.entity.QTotalHitsLog.totalHitsLog;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TotalHitsLogRepositoryImpl implements TotalHitsLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TotalHitsLogRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public long getYesterdayLog(LocalDateTime yesterday) {

        Long result = queryFactory
            .select(totalHitsLog.totalHits.sum())
            .from(totalHitsLog)
            .where(
                totalHitsLog.recordedAt.loe(yesterday)
            )
            .fetchOne();
        return result == null ? 0 : result;
    }

    @Override
    public long getTodayLog(LocalDateTime today) {
        Long result = queryFactory
            .select(totalHitsLog.totalHits.sum())
            .from(totalHitsLog)
            .where(
                totalHitsLog.recordedAt.loe(today)
            )
            .fetchOne();
        return result == null ? 0 : result;
    }

    @Override
    public List<Long> getListLog(LocalDateTime today) {
        List<Long> logs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            logs.add(queryFactory
                .select(totalHitsLog.totalHits.sum())
                .from(totalHitsLog)
                .where(
                    totalHitsLog.recordedAt.loe(today.minusDays(6).plusDays(i).withHour(23).withMinute(59).withSecond(59))
                ).fetchOne());
        }
        return logs;
    }


}
