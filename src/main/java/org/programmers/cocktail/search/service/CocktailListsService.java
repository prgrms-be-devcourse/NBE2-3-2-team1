package org.programmers.cocktail.search.service;

import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository;
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
    private CocktailListsRepository cocktailListsRepository;


    @Autowired
    private CocktailListsMapper cocktailListsMapper;

    public int findByUserIdAndCocktailId(Long userId, Long cocktailId){

        Optional<CocktailLists> cocktailListsOptional = cocktailListsRepository.findByUserIdAndCocktailId(userId, cocktailId);

        if(!cocktailListsOptional.isPresent()){
            return FAIL;
        }
        return SUCCESS;
    }

    public int insertCocktailList(CocktailListsTO cocktailListsTO){

        // TO->Entity 변환
        CocktailLists cocktailLists = cocktailListsMapper.convertToCocktailLists(cocktailListsTO);
        try {
            cocktailListsRepository.save(cocktailLists);
        } catch (Exception e) {
            System.out.println("[에러]"+e.getMessage());
            return FAIL;
        }

        return SUCCESS;
    }

    public int deleteCocktailList(CocktailListsTO cocktailListsTO){

        int cocktailListDeleteResult = cocktailListsRepository.deleteByUserIdAndCocktailId(cocktailListsTO.getUserId(), cocktailListsTO.getCocktailId());

        if(cocktailListDeleteResult==0){
            // 삭제된 행이 없는 경우
            return FAIL;
        }

        return SUCCESS;
    }
}
