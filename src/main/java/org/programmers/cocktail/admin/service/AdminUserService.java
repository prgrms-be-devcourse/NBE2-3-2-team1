package org.programmers.cocktail.admin.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.programmers.cocktail.admin.dto.UserResponse;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.exception.ErrorCode;
import org.programmers.cocktail.global.exception.BadRequestException;
import org.programmers.cocktail.global.response.ApiResponse;
import org.programmers.cocktail.repository.authorities.AuthoritiesRepository;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminUserService {

    private final UsersRepository usersRepository;

    @Autowired
    public AdminUserService(
        UsersRepository usersRepository
    ) {
        this.usersRepository = usersRepository;
    }

    public Page<UserResponse> findAllByAuthorities_Role(String role, Pageable pageable) {
        Page<Users> users = usersRepository.findAllByAuthorities_Role(role, pageable);
        return users.map(UserResponse::new);
    }

    @Transactional
    public void deleteById(long id) {
        try {
            usersRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Page<UserResponse> searchByKeyword(String keyword, Pageable pageable) {
        Page<Users> users = usersRepository.searchByKeyword(keyword, pageable);
        return users.map(UserResponse::new);
    }



}
