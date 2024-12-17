package org.programmers.cocktail.login.repository;

import java.util.List;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.UserRegisterDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginRepository extends JpaRepository<Users, String> {

    @Query(value = "select u from users u where u.email = :email")
    Users findByEmail(String email);

    @Query(value = "select u from users u where u.email = :email and u.password = :password")
    Users findByEmailAndPassword(String email, String password);
}
