package org.programmers.cocktail.repository.cocktails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.programmers.cocktail.entity.QCocktails;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class CocktailsRepositoryImpl implements CocktailsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    private ModelMapper modelMapper;


    public CocktailsRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Long getTotalHits() {
        QCocktails cocktails = QCocktails.cocktails;
        return queryFactory
            .select(cocktails.hits.sum())
            .from(cocktails)
            .fetchOne();
    }

    public List<CocktailsTO> convertToCocktailsTOList(List<Cocktails> cocktailsList) {
        List<CocktailsTO> cocktailsTOList = cocktailsList.stream()
            .map(cocktails -> modelMapper.map(cocktails, CocktailsTO.class))
            .collect(Collectors.toList());
        return cocktailsTOList;
    }

    public Cocktails convertToCocktails(CocktailsTO cocktailsTO) {
        return modelMapper.map(cocktailsTO, Cocktails.class);
    }

    public CocktailsTO convertToCocktailsTO(Cocktails cocktails) {
        return modelMapper.map(cocktails, CocktailsTO.class);
    }
}