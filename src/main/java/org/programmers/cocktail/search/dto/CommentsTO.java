package org.programmers.cocktail.search.dto;

import java.time.LocalDateTime;
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
public class CommentsTO {
    private Long id;
    private String content;
    private Long userId;
    private Long cocktailId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
