package org.programmers.cocktail.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElasticUserRequest {

    private Long id;
    private int age;
    private String gender;

}
