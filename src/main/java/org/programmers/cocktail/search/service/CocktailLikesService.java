package org.programmers.cocktail.search.service;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLikes;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepositoryImpl;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryImpl;
import org.programmers.cocktail.search.dto.CocktailLikesTO;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CocktailLikesService {

    static final int SUCCESS = 1;
    static final int FAIL = 0;

    @Autowired
    private CocktailLikesRepositoryCustom cocktailLikesRepositoryCustom;

    @Autowired
    private CocktailLikesRepositoryImpl cocktailLikesRepositoryImpl;

    public int findByUserIdAndCocktailId(Long userId, Long cocktailId){

        Optional<CocktailLikes> cocktailLikesOptional = cocktailLikesRepositoryCustom.findByUserIdAndCocktailId(userId, cocktailId);

        if(!cocktailLikesOptional.isPresent()){
            return FAIL;
        }
        return SUCCESS;
    }

    public int insertCocktailLikes(CocktailLikesTO cocktailLikesTO){

        // TO->Entity 변환
        CocktailLikes cocktailLikes = cocktailLikesRepositoryImpl.convertToCocktailLikes(cocktailLikesTO);
        try {
            cocktailLikesRepositoryCustom.save(cocktailLikes);
        } catch (Exception e) {
            System.out.println("[에러]"+e.getMessage());
            return FAIL;
        }

        return SUCCESS;
    }

    public int deleteCocktailLikes(CocktailLikesTO cocktailLikesTO){

        int cocktailLikesDeleteResult = cocktailLikesRepositoryCustom.deleteByUserIdAndCocktailId(cocktailLikesTO.getUserId(), cocktailLikesTO.getCocktailId());;

        if(cocktailLikesDeleteResult==0){
            // 삭제된 행이 없는 경우
            return FAIL;
        }

        return SUCCESS;
    }



}
