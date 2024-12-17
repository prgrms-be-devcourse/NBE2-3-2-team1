package org.programmers.cocktail.repository.user;

import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
