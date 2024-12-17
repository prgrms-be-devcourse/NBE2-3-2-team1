package org.programmers.cocktail.admin.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.programmers.cocktail.admin.dto.DashboardCocktailResponse;
import org.programmers.cocktail.admin.service.DashboardService;
import org.programmers.cocktail.entity.Cocktails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin")
public class DashboardWebController {

    private final DashboardService dashboardService;

    public DashboardWebController(final DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(ModelAndView mv) {
        List<Cocktails> list = dashboardService.getCocktailsByLikesDesc();
        List<DashboardCocktailResponse> responses = list.stream()
            .map(cocktail -> DashboardCocktailResponse.builder()
                .name(cocktail.getName())
                .imageUrl(cocktail.getImage_url())
                .hits(cocktail.getHits())
                .likes(cocktail.getLikes())
                .build())
            .toList();

        mv.addObject("list", responses);

        int userCount = dashboardService.getUserCount();
        mv.addObject("userCount", userCount);

        mv.setViewName("dashboard");
        return mv;
    }
}
