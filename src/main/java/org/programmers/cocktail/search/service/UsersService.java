package org.programmers.cocktail.search.service;

import java.util.Optional;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.programmers.cocktail.repository.users.UsersRepositoryImpl;
import org.programmers.cocktail.search.dto.UsersTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersMapper usersMapper;

//    @Autowired
//    private UsersRepositoryImpl usersRepositoryImpl;

    public UsersTO findByEmail(String sessionEmail) {
        Users userInfo =  usersRepository.findByEmail(sessionEmail).orElse(null);
        return usersMapper.convertToUsersTO(userInfo);
    }

}
