package org.programmers.cocktail.repository.comments;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import org.programmers.cocktail.entity.QComments;

public class CommentsRepositoryImpl implements CommentsRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public CommentsRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countTotalCommentsUntilYesterday(LocalDateTime today) {
        QComments comments = QComments.comments;

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);

        return queryFactory
            .select(comments.count())
            .from(comments)
            .where(
                comments.updatedAt.loe(yesterday)
            )
            .fetchOne();
    }
}
