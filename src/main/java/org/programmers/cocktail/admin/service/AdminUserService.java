package org.programmers.cocktail.admin.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.programmers.cocktail.admin.dto.UserResponse;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.exception.ErrorCode;
import org.programmers.cocktail.global.exception.BadRequestException;
import org.programmers.cocktail.global.exception.NotFoundException;
import org.programmers.cocktail.global.exception.DataAccessException;
import org.programmers.cocktail.global.exception.RunTimeException;
import org.programmers.cocktail.global.response.ApiResponse;
import org.programmers.cocktail.repository.authorities.AuthoritiesRepository;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepository;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository;
import org.programmers.cocktail.repository.comments.CommentsRepository;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminUserService {

    private final UsersRepository usersRepository;

    private final CommentsRepository commentsRepository;

    private final CocktailListsRepository cocktaillistsRepository;

    private final CocktailLikesRepository cocktailLikesRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    public AdminUserService(
        UsersRepository usersRepository, CommentsRepository commentsRepository,
        CocktailListsRepository cocktaillistsRepository,
        CocktailLikesRepository cocktailLikesRepository,
        AuthoritiesRepository authoritiesRepository) {
        this.usersRepository = usersRepository;
        this.commentsRepository = commentsRepository;
        this.cocktaillistsRepository = cocktaillistsRepository;
        this.cocktailLikesRepository = cocktailLikesRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    public Page<UserResponse> findAllByAuthorities_Role(String role, Pageable pageable) {
        Page<Users> users = usersRepository.findAllByAuthorities_Role(role, pageable);
        return users.map(UserResponse::new);
    }

    @Transactional
    public void deleteById(long id) {

        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        try {
            cocktailLikesRepository.deleteAllByUsers(user);
            commentsRepository.deleteAllByUsers(user);
            cocktaillistsRepository.deleteAllByUsers(user);

            authoritiesRepository.deleteAllByUsers(user);

            usersRepository.delete(user);

        } catch (DataAccessException e) {
            throw new DataAccessException(ErrorCode.DATABASE_ERROR);
        } catch (Exception e) {
            throw new RunTimeException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Page<UserResponse> searchByKeyword(String keyword, Pageable pageable) {
        Page<Users> users = usersRepository.searchByKeyword(keyword, pageable);
        return users.map(UserResponse::new);
    }



}
