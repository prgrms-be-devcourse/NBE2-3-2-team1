package org.programmers.cocktail.repository.users;


import jakarta.transaction.Transactional;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersRepositoryCustom {

    Page<Users> searchByKeyword(String keyword, Pageable pageable);

}