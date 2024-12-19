package org.programmers.cocktail.search.service;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryImpl;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CocktailListsService {

    static final int SUCCESS = 1;
    static final int FAIL = 0;

    @Autowired
    private CocktailListsRepositoryCustom cocktailListsRepositoryCustom;

    @Autowired
    private CocktailListsRepositoryImpl cocktailListsRepositoryImpl;

    public int findByUserIdAndCocktailId(Long userId, Long cocktailId){

        Optional<CocktailLists> cocktailListsOptional = cocktailListsRepositoryCustom.findByUserIdAndCocktailId(userId, cocktailId);

        if(!cocktailListsOptional.isPresent()){
            return FAIL;
        }
        return SUCCESS;
    }

    public int insertCocktailList(CocktailListsTO cocktailListsTO){

        // TO->Entity 변환
        CocktailLists cocktailLists = cocktailListsRepositoryImpl.convertToCocktailLists(cocktailListsTO);
        System.out.println("cocktailLists: " + cocktailLists);
        try {
            cocktailListsRepositoryCustom.save(cocktailLists);
        } catch (Exception e) {
            System.out.println("[에러]"+e.getMessage());
            return FAIL;
        }

        return SUCCESS;
    }

    public int deleteCocktailList(CocktailListsTO cocktailListsTO){

        System.out.println(cocktailListsTO.getUserId() + " "+ cocktailListsTO.getCocktailId());
        int cocktailListDeleteResult = cocktailListsRepositoryCustom.deleteByUserIdAndCocktailId(cocktailListsTO.getUserId(), cocktailListsTO.getCocktailId());
        System.out.println("cocktailListDeleteResult: " +cocktailListDeleteResult);
        if(cocktailListDeleteResult==0){
            // 삭제된 행이 없는 경우
            return FAIL;
        }

        return SUCCESS;
    }
}
