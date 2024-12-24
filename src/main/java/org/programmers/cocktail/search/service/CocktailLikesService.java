package org.programmers.cocktail.search.service;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLikes;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepository;
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
    private CocktailLikesRepository cocktailLikesRepository;


    @Autowired
    private CocktailLikesMapper cocktailLikesMapper;

    public int findByUserIdAndCocktailId(Long userId, Long cocktailId){

        Optional<CocktailLikes> cocktailLikesOptional = cocktailLikesRepository.findByUserIdAndCocktailId(userId, cocktailId);

        if(!cocktailLikesOptional.isPresent()){
            return FAIL;
        }
        return SUCCESS;
    }

    public int insertCocktailLikes(CocktailLikesTO cocktailLikesTO){

        // TO->Entity 변환
        CocktailLikes cocktailLikes = cocktailLikesMapper.convertToCocktailLikes(cocktailLikesTO);
        try {
            cocktailLikesRepository.save(cocktailLikes);
        } catch (Exception e) {
            System.out.println("[에러]"+e.getMessage());
            return FAIL;
        }

        return SUCCESS;
    }

    public int deleteCocktailLikes(CocktailLikesTO cocktailLikesTO){

        int cocktailLikesDeleteResult = cocktailLikesRepository.deleteByUserIdAndCocktailId(cocktailLikesTO.getUserId(), cocktailLikesTO.getCocktailId());;

        if(cocktailLikesDeleteResult==0){
            // 삭제된 행이 없는 경우
            return FAIL;
        }

        return SUCCESS;
    }

    public Long countCocktailLikesById(CocktailLikesTO cocktailLikesTO){

        //todo 예외처리 -> 에러 발생시 어떻게 처리할지 체크
        // 1) cocktailLikesTO에 cocktailid가 null일때
        // 2)
        Long cocktailLikesCountById = cocktailLikesRepository.countCocktailLikesById(cocktailLikesTO.getCocktailId());

        return cocktailLikesCountById;
    }

}