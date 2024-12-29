package org.programmers.cocktail.list.controller;

import java.util.List;
import org.programmers.cocktail.list.dto.ListCocktail;
import org.programmers.cocktail.list.service.ListService;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/list")
public class ListController {
    @Autowired
    ListService listService;
    @Autowired
    private CocktailsRepository cocktailsRepository;

    @GetMapping("")
    public String list() {
        return "user/top";
    }

    @GetMapping("/show")
    public String show(Model model) {
        List<ListCocktail> list = listService.showList();

        List<ListCocktail> topList = list.size() >10 ? list.subList(0, 10) : list;

        model.addAttribute("list", topList);

        return "user/top";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        // ID로 칵테일 정보를 가져옴
        ListCocktail cocktail = listService.getCocktailById(id);

        // 칵테일 정보를 모델에 추가
        model.addAttribute("cocktail", cocktail);

        // search3.html로 이동
        return "user/search3"; // 상세 페이지를 위한 템플릿 이름
    }
}