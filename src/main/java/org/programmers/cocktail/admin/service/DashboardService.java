package org.programmers.cocktail.admin.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardService {

    private final UsersRepository usersRepository;

    private final CocktailsRepository cocktailsRepository;

    @Autowired
    public DashboardService(UsersRepository usersRepository,
        CocktailsRepository cocktailsRepository) {

        this.usersRepository = usersRepository;
        this.cocktailsRepository = cocktailsRepository;
    }

    public List<Cocktails> getCocktailsByLikesDesc() {
        return cocktailsRepository.findAllByOrderByLikesDesc();
    }

    public int getUserCount() {
        return usersRepository.findAll().size();
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

}
