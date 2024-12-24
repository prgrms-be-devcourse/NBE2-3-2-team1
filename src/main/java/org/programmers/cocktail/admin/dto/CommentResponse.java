package org.programmers.cocktail.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.cocktail.entity.Comments;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private String authorName;

    private String cocktailName;

    public CommentResponse(Comments comments) {
        this.id = comments.getId();
        this.content = comments.getContent();
        this.createdAt = comments.getCreatedAt();
        this.authorName = comments.getUsers().getName();
        this.cocktailName = comments.getCocktails().getName();
    }

}
