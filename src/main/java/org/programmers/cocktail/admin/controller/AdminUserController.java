package org.programmers.cocktail.admin.controller;

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

@Controller
@RequestMapping("/api/users")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable long id) {
        try {
            adminUserService.deleteById(id);
            ApiResponse<Object> response = ApiResponse.createSuccessWithNoData();
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            ApiResponse<Object> response = ApiResponse.createErrorWithMsg(e.getMessage());
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ApiResponse<Object> response = ApiResponse.createErrorWithMsg(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
