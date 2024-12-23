package org.programmers.cocktail.repository.authorities;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.entity.Authorities;
import org.programmers.cocktail.entity.QAuthorities;

@Slf4j
public class AuthoritiesRepositoryImpl implements AuthoritiesRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AuthoritiesRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countTotalUserUntilYesterday(String authority, LocalDateTime to) {
        QAuthorities authorities = QAuthorities.authorities;
        log.info("Parameter to: {}", to);
        LocalDateTime until = to.minusDays(1).withHour(23).withMinute(59).withSecond(59);
        log.info("Calculated until: {}", until);
        return queryFactory
            .select(authorities.id.count())
            .from(authorities)
            .where(
                authorities.role.eq(authority)
                    .and(authorities.createdAt.loe(until))
            )
            .fetchOne();
    }
}
