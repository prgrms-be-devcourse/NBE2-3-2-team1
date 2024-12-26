package org.programmers.cocktail.admin.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.cocktail.admin.service.AdminCommentService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    @Autowired
    private final AdminCommentService adminCommentService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        try{
            adminCommentService.deleteById(id);
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