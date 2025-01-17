package org.programmers.cocktail.repository.users;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.admin.dto.UserRequest;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface UsersRepository extends JpaRepository<Users, Long>, UsersRepositoryCustom {

    @NonNull
    Optional<Users> findById(long id);

    Page<Users> findAllByAuthorities_Role(String role, Pageable pageable);

    boolean deleteById(long id);


    Optional<Users> findByEmail(@NonNull String email);

    @Query(value = "select u from users u where u.email = :email")
    Users findsByEmail(String email);

    @Query(value = "select u from users u where u.email = :email and u.password = :password")
    Users findByEmailAndPassword(String email, String password);

    @Modifying
    @Transactional
    @Query(value = "update users u set u.name = :name, u.password = :password where u.id = :id")
    int updateById(String name, String password, Long id);

    boolean existsByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM users u WHERE u.updatedAt > :lastSyncTime")
    List<Users> findUpdatedSince(@Param("lastSyncTime") LocalDateTime lastSyncTime);


}