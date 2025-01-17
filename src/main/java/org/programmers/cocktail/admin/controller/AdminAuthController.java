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
                return ResponseEntity.status(ErrorCode.INVALID_PASSWORD.getStatus())
                    .body(ApiResponse.createErrorWithMsg(ErrorCode.INVALID_PASSWORD.getMessage()));
            }
        } catch (BadRequestException e) {
            return ResponseEntity.status(ErrorCode.USER_NOT_FOUND.getStatus())
                .body(ApiResponse.createErrorWithMsg(ErrorCode.USER_NOT_FOUND.getMessage()));
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
