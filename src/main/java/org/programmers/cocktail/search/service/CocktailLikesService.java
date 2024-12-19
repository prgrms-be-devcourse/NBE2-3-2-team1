package org.programmers.cocktail.search.service;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLikes;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CocktailLikesService {

    static final int SUCCESS = 1;
    static final int FAIL = 0;

    @Autowired
    private CocktailLikesRepositoryCustom cocktailLikesRepositoryCustom;

//    @Autowired
//    private CocktailListsRepositoryImpl cocktailListsRepositoryImpl;

    public int findByUserIdAndCocktailId(Long userId, Long cocktailId){

        Optional<CocktailLikes> cocktailLikesOptional = cocktailLikesRepositoryCustom.findByUserIdAndCocktailId(userId, cocktailId);

        if(!cocktailLikesOptional.isPresent()){
            return FAIL;
        }
        return SUCCESS;
    }

}
