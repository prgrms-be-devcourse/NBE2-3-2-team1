package org.programmers.cocktail.suggestion.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.programmers.cocktail.suggestion.dto.ChatGPTRequest;
import org.programmers.cocktail.suggestion.dto.ChatGPTResponse;
import org.programmers.cocktail.suggestion.service.CocktailExternalApiImageURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/suggestion")
public class SuggestionController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    CocktailExternalApiImageURL cocktailExternalApiImageURL;

    @GetMapping("")
    public String suggestion() {
        return "user/suggestion";
    }

    @RequestMapping("/detail")
    public String getSuggestion(@RequestParam("query") String query, Model modelObj) throws Exception{
        // Query 분석 및 추천 로직

        String[] answers = query.split("/");
        String answer1 = answers[0];
        String answer2 = answers[1];

        String prompt = "";
        prompt += "suggest one cocktail which is " + answer1 + " and " + answer2 + ". Send me with only JSON format(don't write ```json and depth is less than 2) which only contains name, description, ingredient, instruction, imageURL also each information is going to be json index and do not change the spelling(do not add 's' or something else)";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        System.out.println(chatGPTResponse.getChoices().get(0).getMessage().getContent());

        JSONParser jsonParser = new JSONParser();

        //3. To Object
        Object obj = jsonParser.parse(chatGPTResponse.getChoices().get(0).getMessage().getContent());

        //4. To JsonObject
        JSONObject jsonObj = (JSONObject) obj;

        //print
        System.out.println(jsonObj.get("name")); //sim
        System.out.println(jsonObj.get("description"));
        System.out.println(jsonObj.get("ingredient"));
        System.out.println(jsonObj.get("instruction"));


        String keyword = jsonObj.get("name").toString();
        String cocktailImageURL = cocktailExternalApiImageURL.getCocktailImageURL(keyword);

        // Model에 데이터 추가
        modelObj.addAttribute("cocktailName",jsonObj.get("name"));
        modelObj.addAttribute("cocktailDescription", jsonObj.get("description"));
        modelObj.addAttribute("cocktailIngredient", jsonObj.get("ingredient"));
        modelObj.addAttribute("cocktailInstruction", jsonObj.get("instruction"));
        modelObj.addAttribute("cocktailImageURL", cocktailImageURL);

        // 결과 페이지로 이동
        return "user/result"; // result.html 템플릿 반환
    }

    @RequestMapping("/place")
    public String getSuggestionPlace(@RequestParam("query") String query, Model modelObj) throws Exception{
        // Query 분석 및 추천 로직

        String[] answers = query.split("/");
        String answer1 = answers[0];
        String answer2 = answers[1];

        String prompt = "";
        prompt += "suggest one cocktail which is " + answer1 + " and " + answer2 + ". Send me with only JSON format(don't write ```json and depth is less than 2) which only contains name, description, ingredient, instruction, imageURL also each information is going to be json index and do not change the spelling(do not add 's' or something else)";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        System.out.println(chatGPTResponse.getChoices().get(0).getMessage().getContent());

        JSONParser jsonParser = new JSONParser();

        //3. To Object
        Object obj = jsonParser.parse(chatGPTResponse.getChoices().get(0).getMessage().getContent());

        //4. To JsonObject
        JSONObject jsonObj = (JSONObject) obj;

        //print
        System.out.println(jsonObj.get("name")); //sim
        System.out.println(jsonObj.get("description"));
        System.out.println(jsonObj.get("ingredient"));
        System.out.println(jsonObj.get("instruction"));


        String keyword = jsonObj.get("name").toString();
        String cocktailImageURL = cocktailExternalApiImageURL.getCocktailImageURL(keyword);
        System.out.println(cocktailImageURL);

        // Model에 데이터 추가
        modelObj.addAttribute("cocktailName",jsonObj.get("name"));
        modelObj.addAttribute("cocktailDescription", jsonObj.get("description"));
        modelObj.addAttribute("cocktailIngredient", jsonObj.get("ingredient"));
        modelObj.addAttribute("cocktailInstruction", jsonObj.get("instruction"));
        modelObj.addAttribute("cocktailImageURL", cocktailImageURL);

        // 결과 페이지로 이동
        return "user/result"; // result.html 템플릿 반환
    }
}