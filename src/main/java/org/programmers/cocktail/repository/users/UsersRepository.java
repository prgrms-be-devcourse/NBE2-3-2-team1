package org.programmers.cocktail.repository.users;

import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UsersRepository extends JpaRepository<Users, Long> {

    @NonNull
    Optional<Users> findById(@NonNull Long id);

    Optional<Users> findByEmail(@NonNull String email);
}
