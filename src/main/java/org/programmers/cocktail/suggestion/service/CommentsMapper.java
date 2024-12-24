package org.programmers.cocktail.search.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.programmers.cocktail.search.dto.CommentsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsMapper {
    private ModelMapper modelMapper;

    @Autowired
    public CommentsMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // Comments -> CommentsTO 변환 매핑
        modelMapper.addMappings(new PropertyMap<Comments, CommentsTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUsers().getId());
                map().setCocktailId(source.getCocktails().getId());
                map().setUserName(source.getUsers().getName());
            }
        });

        // CommentsTO -> Comments 변환 매핑
        modelMapper.addMappings(new PropertyMap<CommentsTO, Comments>() {
            @Override
            protected void configure() {
                map().getUsers().setId(source.getUserId());
                map().getCocktails().setId(source.getCocktailId());
            }
        });
    }

    public List<CommentsTO> convertToCommentsTOList(List<Comments> commentsList) {
        List<CommentsTO> commentsTOList = commentsList.stream()
            .map(comments -> modelMapper.map(comments, CommentsTO.class))
            .collect(Collectors.toList());
        return commentsTOList;
    }

    public Comments convertToComments(CommentsTO commentsTO) {
        return modelMapper.map(commentsTO, Comments.class);
    }
}
