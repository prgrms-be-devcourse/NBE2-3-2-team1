package org.programmers.cocktail.admin.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.admin.dto.DashboardCocktailResponse;
import org.programmers.cocktail.admin.service.DashboardService;
import org.programmers.cocktail.admin.service.HitsLogService;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.springframework.data.domain.Page;
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

    /**
     * 대시보드에서 추천수가 가장 많은 칵테일 Top 3를 반환합니다. 카드 형식으로 UserCount, YesterdayUsers, usersGrowthRate를 계산하여
     * users의 성장 추이를 가져옵니다.
     * <p>
     * 카드 형식으로 TotalHits, HitsGrowthRate, YesterdayHits를 계산하여 총 조회수 추이를 가져옵니다.
     * <p>
     * 카드 형식으로 commentCount, CountGrowthRate, YesterdayCounts를 계산하여 총 댓글 수 추이를 가져옵니다.
     */
    @GetMapping("/dashboard")
    public ModelAndView dashboard(ModelAndView mv ) {
        List<DashboardCocktailResponse> list = dashboardService.getCocktailsTopThreeByLikesDesc();

        mv.addObject("list", list);

        long userCount = dashboardService.countByRoleUser();
        mv.addObject("userCount", userCount);

        long yesterdayUsers = dashboardService.countTotalUserUntilYesterday();

        mv.addObject("yesterdayUsers", yesterdayUsers);

        if (yesterdayUsers > 0) {
            mv.addObject("userGrowthRate", (double) userCount / (double) yesterdayUsers);
        } else {
            mv.addObject("userGrowthRate", 0.0);
        }

        long totalHits = dashboardService.getTotalHits();
        mv.addObject("totalHits", totalHits);

        long yesterdayHits = hitsLogService.getYesterdayLog()
            .map(TotalHitsLog::getTotalHits)
            .orElse(0L);

        mv.addObject("yesterdayHits", yesterdayHits);

        if (yesterdayHits > 0) {
            mv.addObject("hitsGrowthRate", (double) totalHits / (double) yesterdayHits);
        } else {
            mv.addObject("hitsGrowthRate", 0.0);
        }

        long commentCount = dashboardService.countComments();

        mv.addObject("commentCount", commentCount);

        long yesterdayComments = dashboardService.countTotalUserUntilYesterday();
        mv.addObject("yesterdayComments", yesterdayComments);

        if (yesterdayComments > 0) {
            mv.addObject("commentGrowthRate", (double) commentCount / (double) yesterdayComments);
        } else {
            mv.addObject("commentGrowthRate", 0.0);
        }

        mv.setViewName("/admin/dashboard");


        return mv;
    }
}
