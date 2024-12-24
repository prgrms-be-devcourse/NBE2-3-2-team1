package org.programmers.cocktail.suggestion.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CocktailsTO {
    private Long id;
    private String name;
    private String ingredients;
    private String description;
    private String recipes;
    private String category;
    private String alcoholic;
    private String image_url;
    private Long hits;
    private Long likes;

}