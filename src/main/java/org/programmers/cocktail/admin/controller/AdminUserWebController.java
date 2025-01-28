package org.programmers.cocktail.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.dto.UserResponse;
import org.programmers.cocktail.admin.service.AdminUserService;
import org.programmers.cocktail.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserWebController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping("")
    public ModelAndView admin_user(ModelAndView mv,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserResponse> userResponsePage = adminUserService.findAllByAuthorities_Role("ROLE_USER", pageable);

        int totalPages = userResponsePage.getTotalPages();
        int currentPage = userResponsePage.getNumber() + 1;

        // 페이지네이션 범위 계산
        int maxPagesToShow = 10; // 한 번에 표시할 최대 페이지 수
        int startPage = Math.max(1, currentPage - maxPagesToShow / 2);
        int endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);

        // 시작 페이지와 끝 페이지가 올바르게 보이도록 조정
        if (endPage - startPage + 1 < maxPagesToShow && endPage == totalPages) {
            startPage = Math.max(1, endPage - maxPagesToShow + 1);
        }

        // ModelAndView에 데이터 추가
        mv.addObject("users", userResponsePage.getContent());
        mv.addObject("totalPages", totalPages);
        mv.addObject("page", currentPage);
        mv.addObject("startPage", startPage);
        mv.addObject("endPage", endPage);

        mv.setViewName("admin/users"); // 반환할 뷰 이름 설정

        return mv;
    }


    @GetMapping("/search")
    public String searchUsers(@RequestParam("keyword") String keyword,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);
        log.info("keyword: {}", keyword);

        Page<UserResponse> userPage = adminUserService.searchByKeyword(keyword, pageable);

        int totalPages = userPage.getTotalPages();
        int currentPage = userPage.getNumber() + 1;

        // 페이지네이션 범위 계산
        int maxPagesToShow = 10; // 한 번에 표시할 최대 페이지 수
        int startPage = Math.max(1, currentPage - maxPagesToShow / 2);
        int endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);

        // 데이터 모델에 추가
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/users";
    }



}
