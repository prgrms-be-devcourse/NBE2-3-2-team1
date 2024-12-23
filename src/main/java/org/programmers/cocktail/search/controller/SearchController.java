package org.programmers.cocktail.search.controller;

import java.util.List;
import org.programmers.cocktail.search.dto.CocktailLikesTO;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.programmers.cocktail.search.dto.CommentsTO;
import org.programmers.cocktail.search.dto.UsersTO;
import org.programmers.cocktail.search.service.CocktailExternalApiService;
import org.programmers.cocktail.search.service.CocktailLikesService;
import org.programmers.cocktail.search.service.CocktailListsService;
import org.programmers.cocktail.search.service.CocktailsService;
import org.programmers.cocktail.search.service.CommentsService;
import org.programmers.cocktail.search.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    CocktailsService cocktailsService;

    @Autowired
    UsersService usersService;

    @Autowired
    CocktailExternalApiService cocktailExternalApiService;

    @Autowired
    CocktailListsService cocktailListsService;

    @Autowired
    CocktailLikesService cocktailLikesService;

    @Autowired
    CommentsService commentsService;

    static final int NOT_LOGGED_IN = 0;
    static final int OPERATION_FAIL = 1;
    static final int OPERATION_SUCCESS = 2;      // FAVORITED, ADD, DELETE
    // todo 로그인 및 userid 가져오는 부분 메서드화
    @GetMapping("/search/cocktails")
    public ResponseEntity<List<CocktailsTO>> getCocktailSearchResults(@RequestParam String userInput) {

        // 검색결과 설정
        String keyword = userInput;

        //1. DB에 검색결과 있는지 확인
        List<CocktailsTO> cocktailSearchList = cocktailsService.findByNameContaining(keyword);

        if(!cocktailSearchList.isEmpty()) {
            //DB에 결과가 있는 경우 반환
            System.out.println("Data exists in Local DB");
            return ResponseEntity.ok(cocktailSearchList);
        }
        //2. (DB에 결과가 없는경우) API에서 검색
        System.out.println("No data in local DB. Fetching Data from Extenal API...");

        // 2-1) 외부 API에서 가져온 cocktail 정보를 DB에 저장
        cocktailSearchList = cocktailExternalApiService.fetchCocktailData(keyword);

        for(CocktailsTO cocktail : cocktailSearchList){
            System.out.println("new cocktail adding to Local DB");
            int cocktailInsertResult = cocktailsService.insertNewCocktailDB(cocktail);

            if(cocktailInsertResult==0){
                // todo 데이터 삽입 실패시 프론트에 alert 띄울지 고민
                System.out.println("[에러] New Cocktail Data Insertion Failed");
            }
        }

        // 2-2) DB에 저장된 데이터를 가져와서 반환
        cocktailSearchList = cocktailsService.findByNameContaining(keyword);

        if(cocktailSearchList.isEmpty()) {
            System.out.println("No matching results found in Local DB and Extenal API");
            // 결과가 없는 경우 204 상태코드 반환
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cocktailSearchList);
    }

    @GetMapping("/favorites/cocktails/{cocktailId}")
    public ResponseEntity<Integer> isFavoritedByUser(@PathVariable String cocktailId){

        // 1. 로그인 상태 확인
        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        String session = "abc@abc.com";
//        String session = "cde@cde.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.ok(NOT_LOGGED_IN);       // 로그인 실패
        }

        // 2. userid 정보가져오기
        UsersTO userInfo = usersService.findByEmail(session);
        if(userInfo==null){
            return ResponseEntity.ok(OPERATION_FAIL);   // 유저 정보 가져올 수 없음
        }

        //3. userid, cocktailid가 cocktail_lists에 존재하는지 확인
        // SUCCESS: 1, FAIL: 0
        int isCocktailListsPresent = cocktailListsService.findByUserIdAndCocktailId(userInfo.getId(), Long.parseLong(cocktailId));

        if(isCocktailListsPresent==0){
            return ResponseEntity.ok(OPERATION_FAIL);     // 즐겨찾기 조회 실패
        }

        return ResponseEntity.ok(OPERATION_SUCCESS);    // 즐겨찾기 조회 성공
    }

    @PostMapping("/favorites/cocktails/{cocktailId}")
    public ResponseEntity<Integer> addFavoritesByUser(@PathVariable String cocktailId){

        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        //1. 로그인 상태 확인
        String session = "abc@abc.com";
//        String session = "cde@cde.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.ok(NOT_LOGGED_IN);        // 로그인 실패
        }

        // 2. userid 정보가져오기
        UsersTO userInfo = usersService.findByEmail(session);
        if(userInfo==null){
            return ResponseEntity.ok(OPERATION_FAIL);   // 유저 정보 가져올 수 없음
        }

        // 3. cocktail_lists에 user_id, cocktail_id 저장
        CocktailListsTO cocktailListsTO = new CocktailListsTO();
        cocktailListsTO.setUserId(userInfo.getId());
        cocktailListsTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int cocktailListInsertResult = cocktailListsService.insertCocktailList(cocktailListsTO);

        if(cocktailListInsertResult==0){
            return ResponseEntity.ok(OPERATION_FAIL);       // DB추가 실패
        }

        return ResponseEntity.ok(OPERATION_SUCCESS);         // DB추가 성공
    }

    @DeleteMapping("/favorites/cocktails/{cocktailId}")
    public ResponseEntity<Integer> deleteFavoritesByUser(@PathVariable String cocktailId){

        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        //1. 로그인 상태 확인
        String session = "abc@abc.com";
//        String session = "cde@cde.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.ok(NOT_LOGGED_IN);        // 로그인 실패
        }

        // 2. userid 정보가져오기
        UsersTO userInfo = usersService.findByEmail(session);
        if(userInfo==null){
            return ResponseEntity.ok(OPERATION_FAIL);   // 유저 정보 가져올 수 없음
        }

        // 3. cocktail_lists에서 user_id, cocktail_id 삭제
        CocktailListsTO cocktailListsTO = new CocktailListsTO();
        cocktailListsTO.setUserId(userInfo.getId());
        cocktailListsTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int cocktailListDeleteResult = cocktailListsService.deleteCocktailList(cocktailListsTO);

        if(cocktailListDeleteResult==0){
            return ResponseEntity.ok(OPERATION_FAIL);       //DB삭제 실패
        }

        return ResponseEntity.ok(OPERATION_SUCCESS);        //DB삭제 성공
    }

    @GetMapping("/likes/cocktails/{cocktailId}")
    public ResponseEntity<Integer> isLikedByUser(@PathVariable String cocktailId) {

        // 1. 로그인 상태 확인
        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        String session = "abc@abc.com";
//        String session = "cde@cde.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.ok(NOT_LOGGED_IN);       // 로그인 실패
        }

        // 2. userid 정보가져오기
        UsersTO userInfo = usersService.findByEmail(session);
        if(userInfo==null){
            return ResponseEntity.ok(OPERATION_FAIL);   // 유저 정보 가져올 수 없음
        }

        //3. userid, cocktailid가 cocktail_likes에 존재하는지 확인
        // SUCCESS: 1, FAIL: 0
        int isCocktailLikesPresent = cocktailLikesService.findByUserIdAndCocktailId(userInfo.getId(), Long.parseLong(cocktailId));

        if(isCocktailLikesPresent==0){
            return ResponseEntity.ok(OPERATION_FAIL);     // 좋아요 조회 실패
        }

        return ResponseEntity.ok(OPERATION_SUCCESS);        // 좋아요 조회 성공
    }

    @PostMapping("/likes/cocktails/{cocktailId}")
    public ResponseEntity<Integer> addLikesByUser(@PathVariable String cocktailId) {

        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        //1. 로그인 상태 확인
        String session = "abc@abc.com";
//        String session = "cde@cde.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.ok(NOT_LOGGED_IN);        // 로그인 실패
        }

        // 2. userid 정보가져오기
        UsersTO userInfo = usersService.findByEmail(session);
        if(userInfo==null){
            return ResponseEntity.ok(OPERATION_FAIL);   // 유저 정보 가져올 수 없음
        }

        // 3. cocktail_likes에 user_id, cocktail_id 저장
        CocktailLikesTO cocktailLikesTO = new CocktailLikesTO();
        cocktailLikesTO.setUserId(userInfo.getId());
        cocktailLikesTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int cocktailLikesInsertResult = cocktailLikesService.insertCocktailLikes(cocktailLikesTO);

        if(cocktailLikesInsertResult==0){
            return ResponseEntity.ok(OPERATION_FAIL);       // DB추가 실패
        }

        // cocktailId에 해당하는 cocktailsLikes 값 가져오기
        Long cocktailLikesCountById = cocktailLikesService.countCocktailLikesById(cocktailLikesTO);

        // cocktails테이블에 cocktailsLikes 값 업데이트
        CocktailsTO cocktailsTO = new CocktailsTO();
        cocktailsTO.setId(Long.parseLong(cocktailId));
        cocktailsTO.setLikes(cocktailLikesCountById);

        int cocktailLikesCountUpdateResult = cocktailsService.updateCocktailLikesCount(cocktailsTO);

        // SUCCESS: 1, FAIL: 0
        if(cocktailLikesCountUpdateResult==0){
            return ResponseEntity.ok(OPERATION_FAIL);     // 칵테일 좋아요 업데이트 실패
        }

        return ResponseEntity.ok(OPERATION_SUCCESS);        // DB추가 성공
    }

    @DeleteMapping("/likes/cocktails/{cocktailId}")
    public ResponseEntity<Integer> deleteLikesByUser(@PathVariable String cocktailId) {

        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        //1. 로그인 상태 확인
        String session = "abc@abc.com";
//        String session = "cde@cde.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.ok(NOT_LOGGED_IN);        // 로그인 실패
        }

        // 2. userid 정보가져오기
        UsersTO userInfo = usersService.findByEmail(session);
        if(userInfo==null){
            return ResponseEntity.ok(OPERATION_FAIL);   // 유저 정보 가져올 수 없음
        }

        // 3. cocktail_likes에서 user_id, cocktail_id 삭제
        CocktailLikesTO cocktailLikesTO = new CocktailLikesTO();
        cocktailLikesTO.setUserId(userInfo.getId());
        cocktailLikesTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int cocktailLikesDeleteResult = cocktailLikesService.deleteCocktailLikes(cocktailLikesTO);

        if(cocktailLikesDeleteResult==0){
            return ResponseEntity.ok(OPERATION_FAIL);       //DB삭제 실패
        }

        // cocktailId에 해당하는 cocktailsLikes 값 가져오기
        Long cocktailLikesCountById = cocktailLikesService.countCocktailLikesById(cocktailLikesTO);

        // cocktails테이블에 cocktailsLikes 값 업데이트
        CocktailsTO cocktailsTO = new CocktailsTO();
        cocktailsTO.setId(Long.parseLong(cocktailId));
        cocktailsTO.setLikes(cocktailLikesCountById);

        int cocktailLikesCountUpdateResult = cocktailsService.updateCocktailLikesCount(cocktailsTO);

        // SUCCESS: 1, FAIL: 0
        if(cocktailLikesCountUpdateResult==0){
            return ResponseEntity.ok(OPERATION_FAIL);     // 칵테일 좋아요 업데이트 실패
        }

        return ResponseEntity.ok(OPERATION_SUCCESS);        //DB삭제 성공
    }

    @GetMapping("/reviews/cocktails/{cocktailId}")
    public ResponseEntity<List<CommentsTO>> loadCocktailComments(@PathVariable String cocktailId) {

        System.out.println("enter loadCocktailComments");

        List<CommentsTO> commentsTOList = commentsService.findByCocktailId(Long.parseLong(cocktailId));

        if(commentsTOList.isEmpty()){
            return ResponseEntity.noContent().build();      // 상태코드 204 전송
        }

        return ResponseEntity.ok(commentsTOList);

    }

    @PostMapping("/reviews/cocktails/{cocktailId}")
    public ResponseEntity<Integer> registerCocktailComments(@PathVariable String cocktailId,
        @RequestBody CommentsTO commentsTOFromClient) {

        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        //1. 로그인 상태 확인
        String session = "abc@abc.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.ok(NOT_LOGGED_IN);        // 로그인 실패
        }

        // 2. userid 정보가져오기
        UsersTO userInfo = usersService.findByEmail(session);
        if(userInfo==null){
            return ResponseEntity.ok(OPERATION_FAIL);   // 유저 정보 가져올 수 없음
        }

        CommentsTO commentsTO = new CommentsTO();
        commentsTO.setContent(commentsTOFromClient.getContent());
        commentsTO.setUserId(userInfo.getId());
        commentsTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        int commentsInsertResult = commentsService.insertComments(commentsTO);

        if(commentsInsertResult==0){
            return ResponseEntity.ok(OPERATION_FAIL);       // DB추가 실패
        }

        return ResponseEntity.ok(OPERATION_SUCCESS);        // DB추가 성공
    }

    @DeleteMapping("/reviews/cocktails/{reviewId}")
    public ResponseEntity<Void> deleteCocktailComments(@PathVariable String reviewId) {

        // todo session.getAttribute("email") HttpSession session 으로 대체 필요
        //1. 로그인 상태 확인
        String session = "abc@abc.com";
        if(session == null){
            //todo 세션을 활용한 로그인 확인 방법 보안 추가 방법 고민
            //todo 어차피 아래에서 session으로 db에 email 있는지 확인하면 이중 보안으로 볼 수 있지 않을까
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();        // 로그인 실패(401반환)
        }

        CommentsTO commentsTO = new CommentsTO();
        commentsTO.setId(Long.parseLong(reviewId));

        int commentsDeleteResult = commentsService.deleteById(commentsTO);

        // SUCCESS: 1, FAIL: 0
        if(commentsDeleteResult==0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // DB추가 실패(500반환))
        }
        return ResponseEntity.noContent().build();        // DB추가 성공(204반환)
    }



}
