package org.programmers.cocktail.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserResponse {
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;  // 이메일, 프로필 정보 포함

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public void setKakaoAccount(
        KakaoAccount kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }

    public static class KakaoAccount {
        private Profile profile;
        private String email;

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public static class Profile {
            private String nickname;
            @JsonProperty("profile_image_url")
            private String image;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }
}




