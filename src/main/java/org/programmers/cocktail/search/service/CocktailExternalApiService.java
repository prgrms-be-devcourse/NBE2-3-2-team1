package org.programmers.cocktail.search.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CocktailExternalApiService {

    @Autowired
    private RestTemplate restTemplate;

    public List<Cocktails> fetchCocktailData(String cocktailName) {
        // API 호출
        String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + cocktailName;
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
            String recipe = drinkNode.get("strInstructions").asText();
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
            cocktails.add(new Cocktails(name, ingredients, recipe, image_url, 0L, 0L));
        }
        return cocktails;
    }
}
