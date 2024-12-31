package org.programmers.cocktail.search.service;

import jakarta.transaction.Transactional;
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
    private CocktailsMapper cocktailsMapper;

    public List<CocktailsTO> findByNameContaining(String keyword) {

        List<Cocktails> cocktailSearchList = cocktailsRepository.findByNameContaining(keyword);

        if(!cocktailSearchList.isEmpty()) {
            return cocktailsMapper.convertToCocktailsTOList(cocktailSearchList);
        }

        return Collections.emptyList();
    }

    public int insertNewCocktailDBbyList(List<CocktailsTO> cocktailsTOList) {
        try {
            //List<TO>->List<Entity> 변환
            List<Cocktails> cocktailsList = cocktailsMapper.convertToCocktailsList(cocktailsTOList);
            cocktailsRepository.saveAll(cocktailsList);
            return SUCCESS;    //저장성공
        } catch (Exception e) {
            System.out.println("[저장실패] : "+e.getMessage());
            return FAIL;       //저장실패
        }
    }

    public CocktailsTO findById(Long cocktailId){
        Cocktails cocktails = cocktailsRepository.findById(cocktailId).orElse(null);
        CocktailsTO cocktailsTO = cocktailsMapper.convertToCocktailsTO(cocktails);

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

    public int updateCocktailLikesCount(CocktailsTO cocktailsTO) {

        Cocktails cocktails = cocktailsRepository.findById(cocktailsTO.getId()).orElse(null);

        if(cocktails == null){
            return FAIL;
        }
        cocktails.setLikes(cocktailsTO.getLikes());

        cocktailsRepository.flush();

        System.out.println(cocktails);

        return SUCCESS;
    }

}
