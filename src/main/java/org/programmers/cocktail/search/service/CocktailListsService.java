package org.programmers.cocktail.search.service;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CocktailListsService {

    @Autowired
    private CocktailListsRepositoryCustom cocktailListsRepositoryCustom;

    @Autowired
    private CocktailListsRepositoryImpl cocktailListsRepositoryImpl;

    public Boolean findByUserIdAndCocktailId(Long userId, Long cocktailId){
        final Boolean SUCCESS = true;
        final Boolean FAIL = false;

        Optional<CocktailLists> cocktailListsOptional = cocktailListsRepositoryCustom.findByUserIdAndCocktailId(userId, cocktailId);

//        System.out.println("test:" + cocktailListsRepositoryImpl.convertToCocktailsListsTO(cocktailListsOptional.get()));
        if(!cocktailListsOptional.isPresent()){
            return FAIL;
        }
        return SUCCESS;
    }
}
