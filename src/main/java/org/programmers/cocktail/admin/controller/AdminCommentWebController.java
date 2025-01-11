package org.programmers.cocktail.admin.controller;

import org.programmers.cocktail.admin.dto.CommentResponse;
import org.programmers.cocktail.admin.service.AdminCommentService;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin/comments")
public class AdminCommentWebController {

    private final AdminCommentService adminCommentService;

    public AdminCommentWebController(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    @GetMapping("")
    public ModelAndView admin_comment(ModelAndView mv,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommentResponse> commentResponses = adminCommentService.findAllComments(pageable);
        mv.addObject("comments", commentResponses.getContent());
        mv.addObject("totalPages", commentResponses.getTotalPages());
        mv.addObject("page", commentResponses.getNumber() + 1);
        mv.setViewName("admin/comments");
        return mv;
    }

    @GetMapping("/search")
    public ModelAndView searchComments(@RequestParam("keyword") String keyword,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        ModelAndView mv) {
        Pageable pageable = PageRequest.of(page - 1, size);
        mv.addObject("keyword", keyword);

        Page<CommentResponse> comments = adminCommentService.searchByKeyword(keyword, pageable);


        mv.addObject("comments", comments.getContent());
        mv.addObject("totalPages", comments.getTotalPages());
        mv.addObject("page", comments.getNumber() + 1);

        mv.setViewName("admin/comments");
        return mv;
    }

}
