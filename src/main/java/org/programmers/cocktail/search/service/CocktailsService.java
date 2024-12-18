package org.programmers.cocktail.search.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.programmers.cocktail.repository.cocktails.CocktailsRepositoryImpl;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CocktailsService {

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

    public Boolean insertNewCocktailDB(CocktailsTO cocktailsTO) {
        try {
            //TO->Entity 변환
            Cocktails cocktails = cocktailsRepositoryImpl.convertToCocktails(cocktailsTO);
            cocktailsRepository.save(cocktails);
            return true;    //저장성공
        } catch (Exception e) {
            System.out.println("[저장실패] : "+e.getMessage());
            return false;       //저장실패
        }
    }

    public Cocktails findById(Long cocktailId){
        Cocktails cocktails = cocktailsRepository.findById(cocktailId).orElse(null);
        return cocktails;
    }
}
