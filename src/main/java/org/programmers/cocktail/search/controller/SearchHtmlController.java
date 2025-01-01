package org.programmers.cocktail.search.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.programmers.cocktail.search.service.CocktailExternalApiService;
import org.programmers.cocktail.search.service.CocktailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchHtmlController {

    @Autowired
    CocktailsService cocktailsService;

    @Autowired
    CocktailExternalApiService cocktailExternalApiService;

    // 검색페이지 반환
    @RequestMapping("/search")
    public String getCocktailSearchPage() {
        // 검색페이지로 이동
        return "user/search1";
    }

    // 검색결과 반환
    @RequestMapping("/search/cocktailresults")
    public String getCocktailSearchResultPage(@RequestParam String userInput, Model model) {

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

        int cocktailInsertByListResult = cocktailsService.insertNewCocktailDBbyList(cocktailSearchList);

        if(cocktailInsertByListResult==0){
            // todo 데이터 삽입 실패시 프론트에 alert 띄울지 고민
            System.out.println("[에러] New Cocktail Data Insertion Failed");
        }

        // 2-2) DB에 저장된 데이터를 가져와서 반환
        cocktailSearchList = cocktailsService.findByNameContaining(keyword);

        if(cocktailSearchList == null || cocktailSearchList.isEmpty()) {
            System.out.println("No matching results found in Local DB and Extenal API");
            model.addAttribute("cocktailSearchList", Collections.emptyList());      // 비어있는 리스트 전달
            return "user/search2";
        }

        model.addAttribute("cocktailSearchList", cocktailSearchList);

        return "user/search2";
    }


    // 추천칵테일 페이지 반환
    @RequestMapping("/recommend")
    public String getRecommendCocktailPage() {
        // 인기 칵테일페이지로 이동
        return "user/suggestion";
    }

    // 메인페이지 반환
    @RequestMapping("/main")
    public String getMainCocktailPage(Model model) {
        List<CocktailsTO> cocktailSearchList = cocktailExternalApiService.fetchCocktailData();
        System.out.println("getMainCocktailPage: " + cocktailSearchList);

        // 1. 랜덤 칵테일 불러오기 실패시
        if(cocktailSearchList == null || cocktailSearchList.isEmpty()) {
            System.out.println("Failed to get random response from Extenal API");
            model.addAttribute("cocktailSearchList", Collections.emptyList());      // 비어있는 리스트 전달
            return "user/main";
        }

        // 2. 랜덤 칵테일 불러오기 성공시 -> 반환
        model.addAttribute("cocktailSearchList", cocktailSearchList);

        return "user/main";
    }

    // 칵테일 상세 페이지 반환
    @RequestMapping("/search/cocktails/{cocktailId}")
    public String getCocktailInfoById(@PathVariable String cocktailId, Model model) {

        // 특정 칵테일 상세페이지 조회시 해당 칵테일 hit 증가
        CocktailsTO cocktailsTO = new CocktailsTO();
        cocktailsTO.setId(Long.parseLong((cocktailId)));

        // SUCCESS: 1, FAIL: 0
        int cocktailHitsUpdateResult = cocktailsService.updateCocktailHits(cocktailsTO);

        if(cocktailHitsUpdateResult==0)
        {
            System.out.println("[에러] Cocktail Hits Update Failed");
        }

        CocktailsTO cocktailsById = cocktailsService.findById(Long.parseLong(cocktailId));
        model.addAttribute("cocktailById", cocktailsById);

        // todo 프론트페이지에서 cocktailById가 null인 경우 alert 띄우도록 처리 필요
        // todo 리스트 상세페이지 구현되면 상세페이지 반환하도록 설정
        return "user/search3";
    }

}
