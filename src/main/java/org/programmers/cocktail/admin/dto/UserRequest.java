package org.programmers.cocktail.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String username;
    private String email;
    private String password;

    public UserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
