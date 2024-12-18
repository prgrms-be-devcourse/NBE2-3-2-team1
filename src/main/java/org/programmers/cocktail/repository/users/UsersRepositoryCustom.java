package org.programmers.cocktail.repository.users;

import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepositoryCustom extends JpaRepository<Users, Long> {

    @Query(value = "select u from users u where u.email = :email")
    Users findByEmail(String email);

    @Query(value = "select u from users u where u.email = :email and u.password = :password")
    Users findByEmailAndPassword(String email, String password);
}