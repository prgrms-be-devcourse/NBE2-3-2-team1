package org.programmers.cocktail.admin.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.dto.CommentResponse;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.exception.ErrorCode;
import org.programmers.cocktail.global.exception.BadRequestException;
import org.programmers.cocktail.global.exception.NotFoundException;
import org.programmers.cocktail.repository.comments.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminCommentService {

    private final CommentsRepository commentsRepository;

    @Autowired
    public AdminCommentService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }


    public Page<CommentResponse> findAllComments(Pageable pageable) {
        Page<Comments> comments = commentsRepository.findAll(pageable);
        log.info(comments.toString());
        return comments.map(CommentResponse::new);
    }

    @Transactional
    public void deleteById(Long id) {
        if (id == null || id <=0) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        boolean isDeleted = commentsRepository.deleteCommentById(id);
        if (!isDeleted) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

    }

    public Page<CommentResponse> searchByKeyword(String keyword, Pageable pageable) {
        Page<Comments> comments = commentsRepository.searchByKeyword(keyword, pageable);
        return comments.map(CommentResponse::new);
    }

}
