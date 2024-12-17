package org.programmers.cocktail.search.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.programmers.cocktail.search.service.CocktailExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    CocktailsRepository cocktailsRepository;

    @Autowired
    CocktailExternalApiService cocktailExternalApiService;

    @GetMapping("/search/cocktails")
    public ResponseEntity<List<Cocktails>> getCocktailSearchResults(@RequestParam String userInput) {

        // 검색결과 설정
        String keyword = userInput;

        //1. DB에 검색결과 있는지 확인
        List<Cocktails> cocktailSearchList = cocktailsRepository.findByNameContaining(keyword);

        if(!cocktailSearchList.isEmpty()) {
            //DB에 결과가 있는 경우 반환
            System.out.println("Data exists in Local DB");
            return ResponseEntity.ok(cocktailSearchList);
        }
        //2. (DB에 결과가 없는경우) API에서 검색
        System.out.println("No data in local DB. Fetching Data from Extenal API...");

        // 2-1) 외부 API에서 가져온 cocktail 정보를 DB에 저장
        cocktailSearchList = cocktailExternalApiService.fetchCocktailData(keyword);


        for(Cocktails cocktail : cocktailSearchList){
            System.out.println("new cocktail added to Local DB");
            cocktailsRepository.save(cocktail);
        }

        // 2-2) DB에 저장된 데이터를 가져와서 반환
        cocktailSearchList = cocktailsRepository.findByNameContaining(keyword);

        if(cocktailSearchList.isEmpty()) {
            System.out.println("No matching results found in Local DB and Extenal API");
            // 결과가 없는 경우 204 상태코드 반환
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cocktailSearchList);
    }

    @GetMapping("/search/cocktails/{cocktailId}")
    public ResponseEntity<Cocktails> getCocktailInfoById(@PathVariable String cocktailId){
        Optional<Cocktails> cocktailByIdOptional = cocktailsRepository.findById(Long.parseLong(cocktailId));
        if(cocktailByIdOptional.isPresent()) {
            Cocktails cocktailById = cocktailByIdOptional.get();
            return ResponseEntity.ok(cocktailById);
        }

        // 결과가 없는 경우 204 상태코드 반환
        return ResponseEntity.noContent().build();
    }

}
