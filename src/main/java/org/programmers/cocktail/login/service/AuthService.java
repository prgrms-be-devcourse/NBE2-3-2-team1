package org.programmers.cocktail.login.service;

import java.util.Collections;
import java.util.Map;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.KakaoUserResponse;
import org.programmers.cocktail.login.dto.LoginResponse;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    @Autowired
    UsersRepository usersRepository;

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    // Spring Framework에서 제공하는 HTTP 클라이언트, 서버 간 HTTP 요청을 보낼 수 있는 도구
    // JSON 또는 XML 데이터를 주고받거나, 외부 API와 통신할 때 유용
    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse register(String code) {
        // 1. 인가 코드를 사용해 Access Token 요청
        String accessToken = getAccessToken(code);
        System.out.println("accessToken: " + accessToken);

        // 2. Access Token을 사용해 사용자 정보 요청
        KakaoUserResponse kakaoUser = getKakaoUser(accessToken);

        Users users = new Users(kakaoUser.getKakaoAccount().getEmail(), kakaoUser.getKakaoAccount().getProfile().getNickname(), "1234");

        if ( usersRepository.findsByEmail(kakaoUser.getKakaoAccount().getEmail()) == null ) {
            usersRepository.save(users);
        }

        // 3. 사용자 정보로 LoginResponse 생성
        return new LoginResponse(
            accessToken,
            null, // Refresh Token은 필요시 추가
            kakaoUser.getKakaoAccount().getProfile().getImage(),
            kakaoUser.getKakaoAccount().getProfile().getNickname(),
            kakaoUser.getKakaoAccount().getEmail()
        );
    }

    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // POST 요청으로 데이터 보내기 - restTemplate
        //JSON 데이터를 요청 본문에 포함해 POST 요청을 보냄
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);
        System.out.println("response getBody: " + response.getBody());
        Map<String, String> responseBody = response.getBody();

        return responseBody.get("access_token");
    }


    private KakaoUserResponse getKakaoUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println("KakaoUserResponse accessToken: " + accessToken);
        headers.setBearerAuth(accessToken); // Authorization: Bearer {accessToken}

        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        //restTemplate.exchange()는 매우 유연한 메서드로, HTTP 요청을 보내고 응답을 원하는 객체 타입으로 변환 가능
        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
            userInfoUri,            // 1. URL (https://kapi.kakao.com/v2/user/me)
            HttpMethod.GET,         // 2. HTTP 메서드 (GET)
            request,                // 3. 요청 엔티티 (헤더)
            KakaoUserResponse.class // 4. 응답 타입 (KakaoUserResponse 객체로 변환)
        );

        System.out.println("HTTP Status Code: " + response.getStatusCode());
        KakaoUserResponse kakaoUserResponse = response.getBody();
        System.out.println("KakaoUserResponse: " + kakaoUserResponse);  // 객체 자체의 toString() 호출
        System.out.println("KakaoUserResponse id: " + kakaoUserResponse.getId());
        System.out.println("User Nickname: " + kakaoUserResponse.getKakaoAccount().getProfile().getNickname());
        System.out.println("User email: " + kakaoUserResponse.getKakaoAccount().getEmail());
        return response.getBody();
    }
}
