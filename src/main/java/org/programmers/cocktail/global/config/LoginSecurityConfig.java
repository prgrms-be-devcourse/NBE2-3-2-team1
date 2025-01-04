package org.programmers.cocktail.global.config;

import org.programmers.cocktail.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class LoginSecurityConfig {

    @Autowired
    private LoginService loginService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                (authorize) -> {
                    authorize
//                        .requestMatchers("/main").permitAll()
//                        .requestMatchers("/register").permitAll()
//                        .requestMatchers("/register_ok").permitAll()
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/login_ok").permitAll()
//                        .requestMatchers("/login_complete").permitAll()
//                        .requestMatchers("/logout_ok").permitAll()
//                        .requestMatchers("/mypage").permitAll()
//                        .requestMatchers("/mypage_ok").permitAll()
//                        .requestMatchers("/modify_ok").permitAll()
//                        .requestMatchers("/withdrawal_ok").permitAll()
//                        .requestMatchers("/withdrawalPage").permitAll()
//                        .requestMatchers("/withdrawalCompletePage").permitAll()
//                        .requestMatchers("/recommend").permitAll()
//                        .requestMatchers("/search/**").permitAll()
//                        .requestMatchers("/api/suggestion/**").permitAll()
//                        .requestMatchers("/popular/**").permitAll() // 이것만 안됨
//                        .requestMatchers("/popular/detail/**").permitAll()
//                        .requestMatchers("/templates/user/**").permitAll()
//                        .requestMatchers("/favorites/**").permitAll()
//                        .requestMatchers("/likes/**").permitAll()
//                        .requestMatchers("/reviews/**").permitAll()
//                        .requestMatchers("/images/**").permitAll()
//                        .requestMatchers("/css/**").permitAll()
//                        .requestMatchers("/kakao_login").permitAll()
//                        .requestMatchers("/login/oauth2/code/kakao/**").permitAll()
                        .anyRequest().permitAll(); // 나머지 요청은 인증 필요
                }
            );
//            .formLogin(
//                (login) -> login
//                    .loginPage("/login")
//                    //.defaultSuccessUrl("/loginsuccess")
//                    .failureUrl("/login")
//            );


        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(loginService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }


}
