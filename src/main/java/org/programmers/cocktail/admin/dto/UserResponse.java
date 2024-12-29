package org.programmers.cocktail.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.cocktail.entity.Users;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponse(final Users users) {
        this.id = users.getId();
        this.name = users.getName();
        this.email = users.getEmail();
        this.createdAt = users.getCreatedAt();
        this.updatedAt = users.getUpdatedAt();
    }

}
