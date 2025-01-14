package org.programmers.cocktail.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Builder.Default
    private int from = 0;
    @Builder.Default
    private int size = 20;

    @Builder.Default
    private boolean includeComments = true;
    @Builder.Default
    private boolean includeLikes = true;

    @Builder.Default
    private int[] ageRanges = {20, 30, 40, 50};

    @Builder.Default
    private int maxKeywords = 50;
    @Builder.Default
    private double minScore = 0.1;

    public ElasticsearchRequest(LocalDateTime timestamp) {
        this.timestamp = timestamp.toString();
    }
}

