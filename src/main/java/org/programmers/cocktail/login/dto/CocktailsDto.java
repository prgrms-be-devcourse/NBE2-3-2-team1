package org.programmers.cocktail.login.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CocktailsDto {

    private Long id;
    private String name;
    private String ingredients;
    private String recipes;
    private String category;
    private String alcoholic;
    private String image_url;
    private Long hits;
    private Long likes;

}
