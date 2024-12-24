package org.programmers.cocktail.repository.users;

import org.programmers.cocktail.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersRepositoryCustom {

    Page<Users> searchByKeyword(String keyword, Pageable pageable);

}
