package org.programmers.cocktail.list.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class ListResponseTO {

    private Long id;
    private String name;
    private String imageUrl;
    private Long likes;
    private LocalDateTime updatedAt;

    public ListResponseTO(String name, String imageUrl, Long likes, LocalDateTime updatedAt) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
