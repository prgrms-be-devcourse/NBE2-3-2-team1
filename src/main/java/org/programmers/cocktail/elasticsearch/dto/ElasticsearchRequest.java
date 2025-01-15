package org.programmers.cocktail.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ElasticsearchRequest {

    private Long userId;
    private int age;
    private String gender;

    private Long cocktailId;
    private String cocktailName;
    private String category;
    private String ingredients;

    private String interactionType;
    private String commentContent;
    private String timestamp;

    public ElasticsearchRequest(LocalDateTime timestamp) {
        this.timestamp = timestamp.toString();
    }
}

