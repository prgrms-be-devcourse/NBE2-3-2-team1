package org.programmers.cocktail.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardCocktailResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Long hits;
    private Long likes;

    public DashboardCocktailResponse(String name, String imageUrl, Long hits, Long likes) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.hits = hits;
        this.likes = likes;
    }

}
