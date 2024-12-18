package org.programmers.cocktail.login.service;

import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.UserRegisterDto;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.programmers.cocktail.repository.users.UsersRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersRepositoryCustom usersRepositoryCustom;

    public int insert(UserRegisterDto to) {
        int flag = 0;

        Users users = new Users(to.getEmail(), to.getName(), to.getPassword());

        usersRepository.save( users );

        return flag;
    }

    public Users findByEmail(@Param("email") String email) {

        Users users = usersRepositoryCustom.findByEmail(email);

        return users;
    }

    public Users selectByEmailandPassword(@Param("email") String email, @Param("password") String password) {

        Users users = usersRepositoryCustom.findByEmailAndPassword(email, password);

        return users;
    }
}