package org.programmers.cocktail.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserRegisterDto {

    private Long id;
    private String name;
    private String email;
    private String password;

}
