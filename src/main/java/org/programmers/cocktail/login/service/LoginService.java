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

    public LoginService(UsersRepository usersRepository,
        UsersRepositoryCustom usersRepositoryCustom) {
        this.usersRepository = usersRepository;
        this.usersRepositoryCustom = usersRepositoryCustom;
    }

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final UsersRepositoryCustom usersRepositoryCustom;


    public int insert(UserRegisterDto to) {
        int flag = 0;

        Users users = new Users(to.getEmail(), to.getName(), to.getPassword());

        this.usersRepository.save( users );

        return flag;
    }

    public Users findByEmail(String email) {

        Users users = this.usersRepositoryCustom.findByEmail(email);

        return users;
    }


    public int updateUser(String name, String password, Long id) {

        int flag = this.usersRepositoryCustom.updateById(name, password, id);

        return flag;
    }


    public int deleteUser(Long id) {

        System.out.println("deleteUser(Long id): " + id + ")");

        this.usersRepository.deleteById(id);

        if ( this.usersRepository.existsById(id) ) {
            return 0;
        } else {
            return 1;
        }
    }

}