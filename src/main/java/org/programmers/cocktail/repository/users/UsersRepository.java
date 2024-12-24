package org.programmers.cocktail.repository.users;

import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UsersRepository extends JpaRepository<Users, Long>, UsersRepositoryCustom {

    Optional<Users> findById(long id);

    Page<Users> findAllByAuthorities_Role(String role, Pageable pageable);

    void deleteById(long id);



}
