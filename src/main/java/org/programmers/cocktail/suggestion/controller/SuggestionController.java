package org.programmers.cocktail.suggestion.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.programmers.cocktail.suggestion.dto.ChatGPTRequest;
import org.programmers.cocktail.suggestion.dto.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/suggestion")
public class SuggestionController {

    // degree of cocktail
    private static String answer1 = null;

    // sweetness of cocktail
    private static String answer2 = null;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/answer1/{answer1}")
    public void answer1(@PathVariable String answer1){
        this.answer1 = answer1;
    }

    @GetMapping("/answer2/{answer2}")
    public void answer2(@PathVariable String answer2){
        this.answer2 = answer2;
    }

    @GetMapping("/chat")
    public String chat() throws ParseException {
        String prompt = "";
        System.out.println(answer1);
        System.out.println(answer2);
        prompt += "suggest one cocktail which is "+answer1+" and "+answer2+". Send me with only JSON format(don't write ```json and depth is less than 2) which only contains name, description, ingredient, instruction";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
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

        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

}