package org.programmers.cocktail.admin.repository;

import org.programmers.cocktail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DashboardUserRepository extends JpaRepository<Users, Long> {

}
