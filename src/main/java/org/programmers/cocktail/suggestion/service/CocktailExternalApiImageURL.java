package org.programmers.cocktail.suggestion.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.programmers.cocktail.suggestion.dto.CocktailsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CocktailExternalApiImageURL {
    @Autowired
    private RestTemplate restTemplate;

    public String getCocktailImageURL(String cocktailName) {
        // API 호출
        String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + cocktailName;
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // JSON 데이터 가져오기
        JsonNode drinksNode = response.getBody().get("drinks");
        if (drinksNode == null || drinksNode.isEmpty()) {
            // 외부 API에 검색결과 없는 경우 빈 리스트 반환
            return "";
        }

        // 검색된 모든 칵테일 처리
        List<CocktailsTO> cocktails = new ArrayList<>();
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
            cocktails.add(new CocktailsTO(0L, name, ingredients,"", recipes, category, alcoholic, image_url, 0L, 0L));
        }
        return cocktails.get(0).getImage_url();
    }
}