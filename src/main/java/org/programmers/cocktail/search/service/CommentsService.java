package org.programmers.cocktail.search.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.repository.comments.CommentsRepository;
import org.programmers.cocktail.search.dto.CommentsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {

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
}
