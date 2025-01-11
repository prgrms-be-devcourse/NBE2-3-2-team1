package org.programmers.cocktail.admin.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.dto.DashboardCocktailResponse;
import org.programmers.cocktail.admin.service.DashboardService;
import org.programmers.cocktail.admin.service.HitsLogService;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.TotalHitsLog;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/admin")
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

        List<Long> usersList = dashboardService.countUserTotalList();

        mv.addObject("usersList", usersList);

        if (yesterdayUsers > 0) {
            double growthRate = (double) userCount / (double) yesterdayUsers;
            String formattedGrowthRate = String.format("%.2f", growthRate);
            mv.addObject("userGrowthRate", formattedGrowthRate);
        } else {
            mv.addObject("userGrowthRate", 0.0);
        }

        long totalHits = hitsLogService.getTodayLog();
        log.info("Total hits: {}", totalHits);
        mv.addObject("totalHits", totalHits);

        long yesterdayHits = hitsLogService.getYesterdayLog();
        log.info("Yesterday hits: {}", yesterdayHits);

        mv.addObject("yesterdayHits", yesterdayHits);

        List<Long> HitsLog = hitsLogService.getListLog();

        mv.addObject("HitsLog", HitsLog);

        if (yesterdayHits > 0) {
            double growthRate = (double) totalHits / (double) yesterdayHits;
            String formattedGrowthRate = String.format("%.2f", growthRate);
            mv.addObject("hitsGrowthRate", formattedGrowthRate);
        } else {
            mv.addObject("hitsGrowthRate", 0.0);
        }

        long commentCount = dashboardService.countComments();

        mv.addObject("commentCount", commentCount);

        long yesterdayComments = dashboardService.countTotalUserUntilYesterday();
        mv.addObject("yesterdayComments", yesterdayComments);

        if (yesterdayComments > 0) {
            double growthRate = (double) commentCount / (double) yesterdayComments;
            String formattedGrowthRate = String.format("%.2f", growthRate);
            mv.addObject("commentGrowthRate", formattedGrowthRate);
        } else {
            mv.addObject("commentGrowthRate", 0.0);
        }

        List<Long> commentsList = dashboardService.countCommentsList();

        mv.addObject("commentsList", commentsList);

        mv.setViewName("/admin/dashboard");


        return mv;
    }
}
