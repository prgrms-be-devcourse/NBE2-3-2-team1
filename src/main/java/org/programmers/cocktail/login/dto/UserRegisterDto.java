package org.programmers.cocktail.login.dto;

import lombok.Getter;
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
    private String gender;
    private int age;

}
