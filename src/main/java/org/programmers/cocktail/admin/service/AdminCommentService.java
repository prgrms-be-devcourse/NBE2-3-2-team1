package org.programmers.cocktail.admin.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.dto.CommentResponse;
import org.programmers.cocktail.entity.Comments;
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
        try {
            commentsRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Page<CommentResponse> searchByKeyword(String keyword, Pageable pageable) {
        Page<Comments> comments = commentsRepository.searchByKeyword(keyword, pageable);
        return comments.map(CommentResponse::new);
    }

}
