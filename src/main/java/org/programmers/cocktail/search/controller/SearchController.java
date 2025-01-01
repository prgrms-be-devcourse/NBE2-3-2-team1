package org.programmers.cocktail.search.controller;

import java.util.List;
import org.programmers.cocktail.global.Utility.SearchUtils;
import org.programmers.cocktail.global.annotation.RequireLogin;
import org.programmers.cocktail.search.dto.CocktailLikesTO;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.programmers.cocktail.search.dto.CommentsTO;
import org.programmers.cocktail.search.enums.ActionType;
import org.programmers.cocktail.search.service.CocktailLikesService;
import org.programmers.cocktail.search.service.CocktailListsService;
import org.programmers.cocktail.search.service.CocktailsService;
import org.programmers.cocktail.search.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;


@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    CocktailListsService cocktailListsService;

    @Autowired
    CocktailLikesService cocktailLikesService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    SearchUtils searchUtils;


    @GetMapping("/favorites/cocktails/{cocktailId}")
    @RequireLogin
    public ResponseEntity<Integer> isFavoritedByUser(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String cocktailId,
        SessionStatus sessionStatus){

        final int PRESENT = 1;
        final int ABSENT = 0;

        //userid, cocktailid가 cocktail_lists에 존재하는지 확인( SUCCESS: 1, FAIL: 0 )
        int isCocktailListsPresent = cocktailListsService.findByUserIdAndCocktailId(searchUtils.searchUserByUserEmail(sessionValue).getId(), Long.parseLong(cocktailId));

        if(isCocktailListsPresent==0){
            return ResponseEntity.ok(ABSENT);// 즐겨찾기 조회 성공(즐겨찾기 없는 경우 - 200 반환)
        }

        return ResponseEntity.ok(PRESENT);   // 즐겨찾기 조회 성공(즐겨찾기 있는 경우 - 200반환)
    }

    @PostMapping("/favorites/cocktails/{cocktailId}")
    @RequireLogin
    public ResponseEntity<Void> addFavoritesByUser(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String cocktailId){

        // cocktail_lists에 user_id, cocktail_id 저장
        CocktailListsTO cocktailListsTO = new CocktailListsTO();
        cocktailListsTO.setUserId(searchUtils.searchUserByUserEmail(sessionValue).getId());
        cocktailListsTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int cocktailListInsertResult = cocktailListsService.insertCocktailList(cocktailListsTO);

        if(cocktailListInsertResult==0){
            throw new RuntimeException("Failed to add a new favorite to the cocktail_lists table"); // DB추가 실패(500반환)
        }

        return ResponseEntity.noContent().build();      //DB추가 성공(204반환)
    }

    @DeleteMapping("/favorites/cocktails/{cocktailId}")
    @RequireLogin
    public ResponseEntity<Void> deleteFavoritesByUser(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String cocktailId){

        // cocktail_lists에서 user_id, cocktail_id 삭제
        CocktailListsTO cocktailListsTO = new CocktailListsTO();
        cocktailListsTO.setUserId(searchUtils.searchUserByUserEmail(sessionValue).getId());
        cocktailListsTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int cocktailListDeleteResult = cocktailListsService.deleteCocktailList(cocktailListsTO);

        if(cocktailListDeleteResult==0){
            throw new RuntimeException("Failed to add a new favorite to the cocktail_lists table"); // DB삭제 실패(500반환)
        }

        return ResponseEntity.noContent().build();      //DB삭제 성공
    }

    @GetMapping("/likes/cocktails/{cocktailId}")
    @RequireLogin
    public ResponseEntity<Integer> isLikedByUser(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String cocktailId) {

        final int PRESENT = 1;
        final int ABSENT = 0;

        // userid, cocktailid가 cocktail_likes에 존재하는지 확인(SUCCESS: 1, FAIL: 0)
        int isCocktailLikesPresent = cocktailLikesService.findByUserIdAndCocktailId(searchUtils.searchUserByUserEmail(sessionValue).getId(), Long.parseLong(cocktailId));

        if(isCocktailLikesPresent==0){
            return ResponseEntity.ok(ABSENT);  // 좋아요 조회 성공(좋아요 없는 경우 - 200 반환)
        }

        return ResponseEntity.ok(PRESENT);       // 좋아요 조회 성공(좋아요 있는 경우 - 200반환)
    }

    @PostMapping("/likes/cocktails/{cocktailId}")
    @RequireLogin
    public ResponseEntity<Long> addLikesByUser(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String cocktailId) {

        return ResponseEntity.ok(cocktailLikesService.updateLikesInfoByUser(sessionValue, cocktailId, ActionType.ADD));      //DB추가 성공(200반환, 좋아요갯수 반환)
    }

    @DeleteMapping("/likes/cocktails/{cocktailId}")
    @RequireLogin
    public ResponseEntity<Long> deleteLikesByUser(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String cocktailId) {

        return ResponseEntity.ok(cocktailLikesService.updateLikesInfoByUser(sessionValue, cocktailId, ActionType.DELETE));      //DB삭제 성공(200반환, 좋아요 갯수 반환)
    }

    @GetMapping("/reviews/cocktails/{cocktailId}")
    public ResponseEntity<List<CommentsTO>> loadCocktailComments(@PathVariable String cocktailId) {

        List<CommentsTO> commentsTOList = commentsService.findByCocktailId(Long.parseLong(cocktailId));

        System.out.println("commentsTOList: "+ commentsTOList);
        if(commentsTOList.isEmpty()||commentsTOList==null){
            System.out.println("commentsTOListisempty");
            return ResponseEntity.noContent().build();      // 상태코드 204 전송
        }

        return ResponseEntity.ok(commentsTOList);

    }

    @PostMapping("/reviews/cocktails/{cocktailId}")
    @RequireLogin
    public ResponseEntity<Void> registerCocktailComments(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String cocktailId,
        @RequestBody CommentsTO commentsTOFromClient) {

        CommentsTO commentsTO = new CommentsTO();
        commentsTO.setContent(commentsTOFromClient.getContent());
        commentsTO.setUserId(searchUtils.searchUserByUserEmail(sessionValue).getId());
        commentsTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int commentsInsertResult = commentsService.insertComments(commentsTO);

        if(commentsInsertResult==0){
            throw new RuntimeException("Failed to add a new Comment to the comments table"); // DB추가 실패(500반환)
        }

        return ResponseEntity.noContent().build();        // DB추가 성공(204반환)
    }

    @DeleteMapping("/reviews/cocktails/{reviewId}")
    @RequireLogin
    public ResponseEntity<Void> deleteCocktailComments(@SessionAttribute(value = "semail", required = false) String sessionValue, @PathVariable String reviewId) {

        CommentsTO commentsTO = new CommentsTO();
        commentsTO.setId(Long.parseLong(reviewId));

        int commentsDeleteResult = commentsService.deleteById(commentsTO);

        // SUCCESS: 1, FAIL: 0
        if(commentsDeleteResult==0){
            throw new RuntimeException("Failed to delete a Comment in the comments table"); // DB삭제 실패(500반환)
        }
        return ResponseEntity.noContent().build();        // DB삭제 성공(204반환)
    }
}
