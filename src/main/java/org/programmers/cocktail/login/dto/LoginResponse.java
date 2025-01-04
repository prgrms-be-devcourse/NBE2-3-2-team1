package org.programmers.cocktail.login.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String accessToken;
    private String refreshToken; // 필요 시
    private String image;
    private String nickname;
    private String email;

    public LoginResponse(String accessToken, String refreshToken,  String image, String nickname, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.image = image;
        this.nickname = nickname;
        this.email = email;
    }
}
