package org.programmers.cocktail.search.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.repository.comments.CommentsRepository;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.programmers.cocktail.search.dto.CommentsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {

    static final int SUCCESS = 1;
    static final int FAIL = 0;

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    CommentsMapper commentsMapper;

    public List<CommentsTO> findByCocktailId(Long cocktailId){

        List<Comments> commentsList = commentsRepository.findByCocktailId(cocktailId);

        if(commentsList.isEmpty()){
            return Collections.emptyList();
        }

        List<CommentsTO> commentsTOList = commentsMapper.convertToCommentsTOList(commentsList);

        return commentsTOList;
    }

    public int insertComments(CommentsTO commentsTO){

        // TO->Entity 변환
        Comments comments = commentsMapper.convertToComments(commentsTO);
        try {
            commentsRepository.save(comments);
        } catch (Exception e) {
            System.out.println("[에러]"+e.getMessage());
            return FAIL;
        }

        return SUCCESS;
    }

    public int deleteById(CommentsTO commentsTO){

        Long commentsId = commentsTO.getId();

        int commentsDeleteResult = commentsRepository.deleteByIdWithReturnAffectedRowCount(commentsId);

        if(commentsDeleteResult == 0){
            return FAIL;
        }

        return SUCCESS;
    }


}
