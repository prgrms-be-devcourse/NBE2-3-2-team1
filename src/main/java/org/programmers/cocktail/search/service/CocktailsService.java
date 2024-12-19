package org.programmers.cocktail.search.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.programmers.cocktail.repository.cocktails.CocktailsRepositoryImpl;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CocktailsService {

    static final int SUCCESS = 1;
    static final int FAIL = 0;

    @Autowired
    private CocktailsRepository cocktailsRepository;

    @Autowired
    private CocktailsRepositoryImpl cocktailsRepositoryImpl;

    public List<CocktailsTO> findByNameContaining(String keyword) {

        List<Cocktails> cocktailSearchList = cocktailsRepository.findByNameContaining(keyword);

        if(!cocktailSearchList.isEmpty()) {
            return cocktailsRepositoryImpl.convertToCocktailsTOList(cocktailSearchList);
        }

        return Collections.emptyList();
    }

    public int insertNewCocktailDB(CocktailsTO cocktailsTO) {
        try {
            //TO->Entity 변환
            Cocktails cocktails = cocktailsRepositoryImpl.convertToCocktails(cocktailsTO);
            cocktailsRepository.save(cocktails);
            return SUCCESS;    //저장성공
        } catch (Exception e) {
            System.out.println("[저장실패] : "+e.getMessage());
            return FAIL;       //저장실패
        }
    }

    public CocktailsTO findById(Long cocktailId){
        Cocktails cocktails = cocktailsRepository.findById(cocktailId).orElse(null);
        CocktailsTO cocktailsTO = cocktailsRepositoryImpl.convertToCocktailsTO(cocktails);

        return cocktailsTO;
    }

    public int updateCocktailHits(CocktailsTO cocktailsTO) {

        Optional<Cocktails> cocktailsOptional = cocktailsRepository.findById(cocktailsTO.getId());

        if(!cocktailsOptional.isPresent()){
            return FAIL;        // 칵테일 불러오기 실패
        }

        Cocktails cocktails = cocktailsOptional.get();
        cocktails.setHits(cocktails.getHits()+1);

        cocktailsRepository.flush();

        return SUCCESS;
    }

}
