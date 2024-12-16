package org.programmers.cocktail.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCocktailResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Long hits;

}
