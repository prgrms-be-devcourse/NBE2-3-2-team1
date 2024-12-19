package org.programmers.cocktail.repository.authorities;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import org.programmers.cocktail.entity.Authorities;
import org.programmers.cocktail.entity.QAuthorities;

public class AuthoritiesRepositoryImpl implements AuthoritiesRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AuthoritiesRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countTotalUserUntilYesterday(String authority, LocalDateTime to) {
        QAuthorities authorities = QAuthorities.authorities;

        LocalDateTime until = to.minusDays(1).withHour(23).withMinute(59).withSecond(59);

        return queryFactory
            .select(authorities.count())
            .from(authorities)
            .where(
                authorities.role.eq(authority)
                    .and(authorities.updatedAt.loe(until))
            )
            .fetchOne();
    }
}
