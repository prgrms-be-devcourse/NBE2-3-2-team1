package org.programmers.cocktail.login.service;

import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.UserRegisterDto;
import org.programmers.cocktail.login.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public int insert(UserRegisterDto to) {
        int flag = 0;

        Users users = new Users(to.getEmail(), to.getName(), to.getPassword());

        loginRepository.save( users );

        return flag;
    }

    public Users findByEmail(@Param("email") String email) {

        Users users = loginRepository.findByEmail(email);

        return users;
    }

    public Users selectByEmailandPassword(@Param("email") String email, @Param("password") String password) {

        Users users = loginRepository.findByEmailAndPassword(email, password);

        return users;
    }
}
