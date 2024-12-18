package org.programmers.cocktail.search.controller;

import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.programmers.cocktail.search.service.CocktailExternalApiService;
import org.programmers.cocktail.search.service.CocktailListsService;
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

//@RestController
@Controller
@RequestMapping("/api")
public class SearchController {

    @Autowired
    CocktailsService cocktailsService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CocktailExternalApiService cocktailExternalApiService;

    @Autowired
    CocktailListsService cocktailListsService;

    @GetMapping("/favorites/cocktails/{cocktailId}")
    @ResponseBody
    public ResponseEntity<Integer> isFavoritedByUser(@PathVariable String cocktailId){

        final int NOT_LOGGED_IN = 0;
        final int NO_DB_INFO = 1;
        final int FAVORITED = 2;

        // 1. 로그인 상태 확인
        String session = "abc@abc.com";         // session.getAttribute("email") HttpSession session 으로 대체 필요
        if(session == null || !session.equals("abc@abc.com")){
            return ResponseEntity.ok(NOT_LOGGED_IN);
        }

        // 2. userid 정보가져오기
        Optional<Users> userInfoOptional = usersRepository.findByEmail(session);
        if(!userInfoOptional.isPresent()){
            return ResponseEntity.ok(NO_DB_INFO);   // 유저 정보 가져올 수 없음
        }

        //3. userid, cocktailid가 cocktail_lists에 존재하는지 확인
        Boolean isCocktailListsPresent = cocktailListsService.findByUserIdAndCocktailId(userInfoOptional.get().getId(), Long.parseLong(cocktailId));

        if(!isCocktailListsPresent){
            return ResponseEntity.ok(NO_DB_INFO);     // CocktailList 정보 가져올 수 없음
        }

        return ResponseEntity.ok(FAVORITED);    // 즐겨찾기 등록되어있는 경우
    }

    @GetMapping("/search/cocktails")
    @ResponseBody
    public ResponseEntity<List<CocktailsTO>> getCocktailSearchResults(@RequestParam String userInput) {

        // 검색결과 설정
        String keyword = userInput;

        //1. DB에 검색결과 있는지 확인
        List<CocktailsTO> cocktailSearchList = cocktailsService.findByNameContaining(keyword);

        if(!cocktailSearchList.isEmpty()) {
            //DB에 결과가 있는 경우 반환
            System.out.println("Data exists in Local DB");
            return ResponseEntity.ok(cocktailSearchList);
        }
        //2. (DB에 결과가 없는경우) API에서 검색
        System.out.println("No data in local DB. Fetching Data from Extenal API...");

        // 2-1) 외부 API에서 가져온 cocktail 정보를 DB에 저장
        cocktailSearchList = cocktailExternalApiService.fetchCocktailData(keyword);

        for(CocktailsTO cocktail : cocktailSearchList){
            System.out.println("new cocktail adding to Local DB");
            cocktailsService.insertNewCocktailDB(cocktail);
        }

        // 2-2) DB에 저장된 데이터를 가져와서 반환
        cocktailSearchList = cocktailsService.findByNameContaining(keyword);

        if(cocktailSearchList.isEmpty()) {
            System.out.println("No matching results found in Local DB and Extenal API");
            // 결과가 없는 경우 204 상태코드 반환
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cocktailSearchList);
    }

    @RequestMapping("/search/cocktails/{cocktailId}")
    public String getCocktailInfoById(@PathVariable String cocktailId, Model model) {
        Cocktails cocktailsById = cocktailsService.findById(Long.parseLong(cocktailId));
        model.addAttribute("cocktailById", cocktailsById);

        // 프론트페이지에서 cocktailById가 null인 경우 alert 띄우도록 처리 필요
        return "favorites";
    }
}
