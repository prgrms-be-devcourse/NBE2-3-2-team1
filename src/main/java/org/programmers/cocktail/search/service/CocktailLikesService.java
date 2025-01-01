package org.programmers.cocktail.search.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLikes;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.global.Utility.SearchUtils;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepository;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepositoryImpl;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryCustom;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepositoryImpl;
import org.programmers.cocktail.search.dto.CocktailLikesTO;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.programmers.cocktail.search.enums.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CocktailLikesService {

    static final int SUCCESS = 1;
    static final int FAIL = 0;

    @Autowired
    private CocktailLikesRepository cocktailLikesRepository;

    @Autowired
    private CocktailLikesMapper cocktailLikesMapper;

    @Autowired
    private SearchUtils  searchUtils;

    @Autowired
    private CocktailsService cocktailsService;

    @Transactional
    public Long updateLikesInfoByUser(String sessionValue, String cocktailId, ActionType actionType) {
        // 1. cocktail_likes에서 user_id, cocktail_id 삭제
        CocktailLikesTO cocktailLikesTO = new CocktailLikesTO();
        cocktailLikesTO.setUserId(searchUtils.searchUserByUserEmail(sessionValue).getId());
        cocktailLikesTO.setCocktailId(Long.parseLong(cocktailId));

        // SUCCESS: 1, FAIL: 0
        if(actionType == ActionType.ADD){
            int cocktailLikesInsertResult = insertCocktailLikes(cocktailLikesTO);

            if(cocktailLikesInsertResult==0){
                throw new RuntimeException("Failed to add a new like to the cocktail_likes table"); // DB추가 실패(500반환)
            }
        }
        else{
            int cocktailLikesDeleteResult = deleteCocktailLikes(cocktailLikesTO);

            if(cocktailLikesDeleteResult==0){
                throw new RuntimeException("Failed to delete a like in cocktail_likes table"); // DB삭제 실패(500반환)
            }
        }

        // 2. cocktailId에 해당하는 cocktailsLikes 값 가져오기
        Long cocktailLikesCountById = countCocktailLikesById(cocktailLikesTO);

        // 3. cocktails테이블에 cocktailsLikes 값 업데이트
        CocktailsTO cocktailsTO = new CocktailsTO();
        cocktailsTO.setId(Long.parseLong(cocktailId));
        cocktailsTO.setLikes(cocktailLikesCountById);

        int cocktailLikesCountUpdateResult = cocktailsService.updateCocktailLikesCount(cocktailsTO);

        // SUCCESS: 1, FAIL: 0
        if(cocktailLikesCountUpdateResult==0){
            throw new RuntimeException("Failed to update likes in cocktails table"); // 칵테일 좋아요 업데이트 실패(500반환)
        }

        return cocktailLikesCountById;
    }


    public int findByUserIdAndCocktailId(Long userId, Long cocktailId){

        Optional<CocktailLikes> cocktailLikesOptional = cocktailLikesRepository.findByUserIdAndCocktailId(userId, cocktailId);

        if(!cocktailLikesOptional.isPresent()){
            return FAIL;
        }
        return SUCCESS;
    }

    public int insertCocktailLikes(CocktailLikesTO cocktailLikesTO){

        // TO->Entity 변환
        CocktailLikes cocktailLikes = cocktailLikesMapper.convertToCocktailLikes(cocktailLikesTO);
        try {
            cocktailLikesRepository.save(cocktailLikes);
        } catch (Exception e) {
            System.out.println("[에러]"+e.getMessage());
            return FAIL;
        }

        return SUCCESS;
    }

    public int deleteCocktailLikes(CocktailLikesTO cocktailLikesTO){

        int cocktailLikesDeleteResult = cocktailLikesRepository.deleteByUserIdAndCocktailId(cocktailLikesTO.getUserId(), cocktailLikesTO.getCocktailId());;

        if(cocktailLikesDeleteResult==0){
            // 삭제된 행이 없는 경우
            return FAIL;
        }

        return SUCCESS;
    }

    public Long countCocktailLikesById(CocktailLikesTO cocktailLikesTO){

        //todo 예외처리 -> 에러 발생시 어떻게 처리할지 체크
        // 1) cocktailLikesTO에 cocktailid가 null일때
        // 2)
        Long cocktailLikesCountById = cocktailLikesRepository.countCocktailLikesById(cocktailLikesTO.getCocktailId());

        return cocktailLikesCountById;
    }

}
