package org.programmers.cocktail.repository.authorities;

import org.programmers.cocktail.entity.Authorities;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long>, AuthoritiesRepositoryCustom {

    int countByRole(String role);

    void deleteAllByUsers(Users users);
}
