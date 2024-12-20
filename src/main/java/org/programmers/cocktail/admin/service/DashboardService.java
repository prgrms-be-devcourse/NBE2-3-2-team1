package org.programmers.cocktail.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.admin.dto.DashboardCocktailResponse;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.repository.authorities.AuthoritiesRepository;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.programmers.cocktail.repository.comments.CommentsRepository;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardService {

    private final UsersRepository usersRepository;

    private final CocktailsRepository cocktailsRepository;

    private final AuthoritiesRepository authoritiesRepository;

    private final CommentsRepository commentsRepository;

    @Autowired
    public DashboardService(UsersRepository usersRepository,
        CocktailsRepository cocktailsRepository, AuthoritiesRepository authoritiesRepository,
        CommentsRepository commentsRepository) {

        this.usersRepository = usersRepository;
        this.cocktailsRepository = cocktailsRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.commentsRepository = commentsRepository;
    }

    public List<DashboardCocktailResponse> getCocktailsTopThreeByLikesDesc() {
        List<Cocktails> list = cocktailsRepository.findAllByOrderByLikesDesc();
        return list.stream()
            .limit(3)
            .map(cocktail -> DashboardCocktailResponse.builder()
                .name(cocktail.getName())
                .imageUrl(cocktail.getImage_url())
                .hits(cocktail.getHits())
                .likes(cocktail.getLikes())
                .build())
            .toList();
    }

    public int countByRoleUser() {
        return authoritiesRepository.countByRole("ROLE_USER");
    }

    public Long countComments() {
        return (long) commentsRepository.findAll().size();
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    public Long getTotalHits() {
        return cocktailsRepository.getTotalHits();
    }

    public Long countTotalUserUntilYesterday() {
        return authoritiesRepository.countTotalUserUntilYesterday("USER_ROLE", LocalDateTime.now());
    }

    public Long countTotalCommentsUntilYesterday() {
        return commentsRepository.countTotalCommentsUntilYesterday(LocalDateTime.now());
    }

}
