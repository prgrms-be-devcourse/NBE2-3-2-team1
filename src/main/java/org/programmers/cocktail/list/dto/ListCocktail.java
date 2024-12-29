package org.programmers.cocktail.list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ListCocktail {
    private Long id;
    private String name;
    private String image_url;
    private Long likes;
}
