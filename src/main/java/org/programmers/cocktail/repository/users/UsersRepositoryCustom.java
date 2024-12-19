package org.programmers.cocktail.repository.users;

import jakarta.transaction.Transactional;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepositoryCustom extends JpaRepository<Users, Long> {

    @Query(value = "select u from users u where u.email = :email")
    Users findByEmail(String email);

    @Query(value = "select u from users u where u.email = :email and u.password = :password")
    Users findByEmailAndPassword(String email, String password);

    @Modifying
    @Transactional
    @Query(value = "update users u set u.name = :name, u.password = :password where u.id = :id")
    int updateById(String name, String password, Long id);

}