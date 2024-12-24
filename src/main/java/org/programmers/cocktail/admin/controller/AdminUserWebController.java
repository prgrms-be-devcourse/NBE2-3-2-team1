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
        mv.addObject("users", userResponsePage.getContent());
        mv.addObject("totalPages" , userResponsePage.getTotalPages());
        mv.addObject("page", userResponsePage.getNumber() + 1);

        return mv;
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("keyword") String keyword,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);
        log.info("keyword: {}", keyword);
        model.addAttribute("keyword", keyword);

        Page<UserResponse> userPage = adminUserService.searchByKeyword(keyword, pageable);


        model.addAttribute("users", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("page", userPage.getNumber() + 1);

        return "admin/users";
    }


}
