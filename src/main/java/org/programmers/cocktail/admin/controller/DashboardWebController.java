package org.programmers.cocktail.admin.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.admin.dto.DashboardCocktailResponse;
import org.programmers.cocktail.admin.service.DashboardService;
import org.programmers.cocktail.admin.service.HitsLogService;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin")
public class DashboardWebController {

    private final DashboardService dashboardService;

    private final HitsLogService hitsLogService;

    public DashboardWebController(final DashboardService dashboardService,
        HitsLogService hitsLogService) {
        this.dashboardService = dashboardService;
        this.hitsLogService = hitsLogService;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(ModelAndView mv) {
        List<DashboardCocktailResponse> list = dashboardService.getCocktailsTopThreeByLikesDesc();

        mv.addObject("list", list);

        int userCount = dashboardService.countByRoleUser();
        mv.addObject("userCount", userCount);

        long commentCount = dashboardService.countComments();
        mv.addObject("commentCount", commentCount);

        long totalHits = dashboardService.getTotalHits();
        mv.addObject("totalHits", totalHits);

        long yesterdayHits = hitsLogService.getYesterdayLog()
            .map(TotalHitsLog::getTotalHits)
            .orElse(0L);

        mv.addObject("yesterdayHits", yesterdayHits);

        mv.setViewName("dashboard");
        return mv;
    }
}
