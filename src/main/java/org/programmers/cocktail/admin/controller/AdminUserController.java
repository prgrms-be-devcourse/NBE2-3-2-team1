package org.programmers.cocktail.admin.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.cocktail.admin.service.AdminUserService;
import org.programmers.cocktail.global.exception.BadRequestException;
import org.programmers.cocktail.global.exception.NotFoundException;
import org.programmers.cocktail.global.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AdminUserController {

    @Autowired
    private final AdminUserService adminUserService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable long id) {
        adminUserService.deleteById(id);
        return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoData());
    }
}
