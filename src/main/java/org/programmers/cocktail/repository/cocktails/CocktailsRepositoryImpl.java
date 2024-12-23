package org.programmers.cocktail.repository.cocktails;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.programmers.cocktail.entity.QCocktails;
import org.springframework.stereotype.Repository;

@Repository
public class CocktailsRepositoryImpl implements CocktailsRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public CocktailsRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Long getTotalHits() {
        QCocktails cocktails = QCocktails.cocktails;
        Long sum = queryFactory
            .select(cocktails.hits.sum())
            .from(cocktails)
            .fetchOne();

        return sum != null ? sum : 0L;
    }

}
