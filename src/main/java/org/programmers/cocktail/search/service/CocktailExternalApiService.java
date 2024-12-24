package org.programmers.cocktail.search.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.repository.cocktails.CocktailsRepositoryImpl;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CocktailExternalApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CocktailsMapper cocktailsMapper;

    // 1. Cocktail 검색용
    public List<CocktailsTO> fetchCocktailData(String cocktailName) {

        System.out.println("[Test] fetchCocktailData with parameter");

        // TheCocktailDB 호출 url - "Search cocktail by name" method
        String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + cocktailName;

        return parseJsonToCocktailsTOList(url);
    }

    // 2. 메인 페이지 출력용
    public List<CocktailsTO> fetchCocktailData() {

        System.out.println("[Test] fetchCocktailData without parameter");
        // TheCocktailDB 호출 url - "Lookup a random cocktail" method
        String url = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

        return parseJsonToCocktailsTOList(url);
    }

    public List<CocktailsTO> parseJsonToCocktailsTOList (String url){
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // JSON 데이터 가져오기
        JsonNode drinksNode = response.getBody().get("drinks");
        if (drinksNode == null || drinksNode.isEmpty()) {
            // 외부 API에 검색결과 없는 경우 빈 리스트 반환
            return Collections.emptyList();
        }

        // 검색된 모든 칵테일 처리
        List<Cocktails> cocktails = new ArrayList<>();
        for (JsonNode drinkNode : drinksNode) {
            String name = drinkNode.get("strDrink").asText();
            StringBuilder ingredientsBuilder = new StringBuilder();
            String recipes = drinkNode.get("strInstructions").asText();
            String category = drinkNode.get("strCategory").asText();
            String alcoholic = drinkNode.get("strAlcoholic").asText();
            String image_url = drinkNode.get("strDrinkThumb").asText();
            // 재료와 측정값 결합
            for (int i = 1; i <= 15; i++) {
                JsonNode ingredientNode = drinkNode.get("strIngredient" + i);
                JsonNode measureNode = drinkNode.get("strMeasure" + i);
                if (ingredientNode == null || ingredientNode.isNull()) {
                    break; // 재료가 없으면 중단
                }
                String ingredient = ingredientNode.asText();
                String measure = measureNode != null && !measureNode.isNull() ? measureNode.asText() : "";
                ingredientsBuilder.append(ingredient).append(" ").append(measure).append(", ");
            }

            // 마지막 쉼표 제거
            String ingredients = ingredientsBuilder.toString().replaceAll(", $", "");

            // ProcessedCocktail 객체 생성
            cocktails.add(new Cocktails(name, ingredients, recipes, category, alcoholic, image_url, 0L, 0L));
        }
        return cocktailsMapper.convertToCocktailsTOList(cocktails);
    }

}