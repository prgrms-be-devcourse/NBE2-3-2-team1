package org.programmers.cocktail.admin.controller;

import org.programmers.cocktail.admin.dto.CommentResponse;
import org.programmers.cocktail.admin.service.AdminCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/comments")
public class AdminCommentWebController {

    private final AdminCommentService adminCommentService;

    public AdminCommentWebController(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    @GetMapping("")
    public ModelAndView admin_comment(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        ModelAndView mv
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommentResponse> commentResponses = adminCommentService.findAllComments(pageable);

        int totalPages = commentResponses.getTotalPages();
        int currentPage = commentResponses.getNumber() + 1;

        // 페이지네이션 범위 계산
        int maxPagesToShow = 10;
        int startPage = Math.max(1, currentPage - maxPagesToShow / 2);
        int endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);

        if (endPage - startPage + 1 < maxPagesToShow && endPage == totalPages) {
            startPage = Math.max(1, endPage - maxPagesToShow + 1);
        }

        mv.addObject("comments", commentResponses.getContent());
        mv.addObject("totalPages", totalPages);
        mv.addObject("page", currentPage);
        mv.addObject("startPage", startPage);
        mv.addObject("endPage", endPage);

        mv.setViewName("admin/comments");
        return mv;
    }

    @GetMapping("/search")
    public ModelAndView searchComments(
        @RequestParam("keyword") String keyword,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        ModelAndView mv
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommentResponse> comments = adminCommentService.searchByKeyword(keyword, pageable);

        int totalPages = comments.getTotalPages();
        int currentPage = comments.getNumber() + 1;

        // 페이지네이션 범위 계산
        int maxPagesToShow = 10;
        int startPage = Math.max(1, currentPage - maxPagesToShow / 2);
        int endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);

        if (endPage - startPage + 1 < maxPagesToShow && endPage == totalPages) {
            startPage = Math.max(1, endPage - maxPagesToShow + 1);
        }

        mv.addObject("comments", comments.getContent());
        mv.addObject("keyword", keyword);
        mv.addObject("totalPages", totalPages);
        mv.addObject("page", currentPage);
        mv.addObject("startPage", startPage);
        mv.addObject("endPage", endPage);

        mv.setViewName("admin/comments");
        return mv;
    }
}
