package org.programmers.cocktail.admin.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.cocktail.admin.dto.UserRequest;
import org.programmers.cocktail.admin.service.AdminAuthService;
import org.programmers.cocktail.exception.ErrorCode;
import org.programmers.cocktail.global.exception.BadRequestException;
import org.programmers.cocktail.global.exception.GlobalExceptionHandler;
import org.programmers.cocktail.global.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authority")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginSubmit(
        @RequestParam String email,
        @RequestParam String password
    ) {
        UserRequest userRequest = new UserRequest(email, password);
        try {
            boolean authenticated = adminAuthService.findByEmailAndPassword(userRequest);
            if (authenticated) {
                return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoData());
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.createErrorWithMsg("비밀번호가 올바르지 않습니다."));
            }
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(ApiResponse.createErrorWithMsg("일치하는 아이디가 없습니다."));
        }

    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Object>> joinSubmit(
        @RequestParam String name,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String confirmPassword
    ) {
        return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoData());
    }

}
