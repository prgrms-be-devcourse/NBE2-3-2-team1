package org.programmers.cocktail.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CocktailsTO {
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
