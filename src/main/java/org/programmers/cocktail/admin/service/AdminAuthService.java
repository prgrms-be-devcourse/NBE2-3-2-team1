package org.programmers.cocktail.admin.service;

import org.programmers.cocktail.admin.dto.UserRequest;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.exception.ErrorCode;
import org.programmers.cocktail.global.exception.BadRequestException;
import org.programmers.cocktail.global.response.ApiResponse;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    public final BCryptPasswordEncoder passwordEncoder;

    private final UsersRepository usersRepository;
    public AdminAuthService(BCryptPasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    public void authenticate(String email, String password) {
        Users user = usersRepository.findByEmail(email)
            .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
        }
    }

}
