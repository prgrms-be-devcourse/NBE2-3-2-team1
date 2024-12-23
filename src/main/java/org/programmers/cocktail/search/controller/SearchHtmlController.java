package org.programmers.cocktail.search.controller;

import java.util.Collections;
import java.util.List;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.programmers.cocktail.search.service.CocktailExternalApiService;
import org.programmers.cocktail.search.service.CocktailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchHtmlController {

    @Autowired
    CocktailsService cocktailsService;

    @Autowired
    CocktailExternalApiService cocktailExternalApiService;

    // 검색페이지 반환
    @RequestMapping("/search")
    public String cocktailSearchPage() {
        // 검색페이지로 이동
        return "user/search1";
    }

    // 검색결과 반환
    @RequestMapping("/search/cocktailresults")
    public String cocktailSearchResultPage(@RequestParam String userInput, Model model) {

        System.out.println("userInput: " + userInput);
        // 검색결과 설정
        String keyword = userInput;

        //1. DB에 검색결과 있는지 확인
        List<CocktailsTO> cocktailSearchList = cocktailsService.findByNameContaining(keyword);

        if(!cocktailSearchList.isEmpty()) {
            //DB에 결과가 있는 경우 반환
            System.out.println("Data exists in Local DB");
            model.addAttribute("cocktailSearchList", cocktailSearchList);
            return "user/search2";
        }
        //2. (DB에 결과가 없는경우) API에서 검색
        System.out.println("No data in local DB. Fetching Data from Extenal API...");

        // 2-1) 외부 API에서 가져온 cocktail 정보를 DB에 저장
        cocktailSearchList = cocktailExternalApiService.fetchCocktailData(keyword);

        for(CocktailsTO cocktail : cocktailSearchList){
            System.out.println("new cocktail adding to Local DB");
            int cocktailInsertResult = cocktailsService.insertNewCocktailDB(cocktail);

            if(cocktailInsertResult==0){
                // todo 데이터 삽입 실패시 프론트에 alert 띄울지 고민
                System.out.println("[에러] New Cocktail Data Insertion Failed");
            }
        }

        // 2-2) DB에 저장된 데이터를 가져와서 반환
        cocktailSearchList = cocktailsService.findByNameContaining(keyword);

        if(cocktailSearchList == null || cocktailSearchList.isEmpty()) {
            System.out.println("No matching results found in Local DB and Extenal API");
            // 결과가 없는 경우 204 상태코드 반환
            model.addAttribute("cocktailSearchList", Collections.emptyList());      // 비어있는 리스트 전달
            return "user/search2";
        }

        model.addAttribute("cocktailSearchList", cocktailSearchList);

        return "user/search2";
    }

    // 인기칵테일 페이지 반환
    @RequestMapping("/popular")
    public String popularCocktailPage() {
        // 인기 칵테일페이지로 이동
        return "user/top";
    }

    // 메인페이지 반환
    @RequestMapping("/main")
    public String mainCocktailPage() {
        // 메인페이지로 이동
        return "user/main";
    }

    // todo 추천칵테일 반환 컨트롤러 추가 및 프론트 페이지 연결

    // 칵테일 상세 페이지 반환
    @RequestMapping("/search/cocktails/{cocktailId}")
    public String getCocktailInfoById(@PathVariable String cocktailId, Model model) {
        System.out.println("cocktailId : " +cocktailId);
        CocktailsTO cocktailsById = cocktailsService.findById(Long.parseLong(cocktailId));

        System.out.println(cocktailsById);
        model.addAttribute("cocktailById", cocktailsById);
        // 특정 칵테일 상세페이지 조회시 해당 칵테일 hit 증가
        CocktailsTO cocktailsTO = new CocktailsTO();
        cocktailsTO.setId(Long.parseLong((cocktailId)));

        // SUCCESS: 1, FAIL: 0
        int cocktailHitsUpdateResult = cocktailsService.updateCocktailHits(cocktailsTO);

        if(cocktailHitsUpdateResult==0)
        {
            System.out.println("[에러] Cocktail Hits Update Failed");
        }

        // todo 프론트페이지에서 cocktailById가 null인 경우 alert 띄우도록 처리 필요
        // todo 리스트 상세페이지 구현되면 상세페이지 반환하도록 설정
        return "user/search3";
    }

}
