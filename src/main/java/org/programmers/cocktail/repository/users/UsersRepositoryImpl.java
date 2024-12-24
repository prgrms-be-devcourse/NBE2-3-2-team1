package org.programmers.cocktail.repository.users;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.programmers.cocktail.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class UsersRepositoryImpl implements UsersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Users> searchByKeyword(String keyword, Pageable pageable) {
        String jpql = "select u from users u where lower(u.name) like :keyword or lower(u.email) like :keyword";

        List<Users> results = entityManager.createQuery(jpql, Users.class)
            .setParameter("keyword", "%" + keyword.toLowerCase() + "%")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        String countJpql = "select count(u) from users u where lower(u.name) like :keyword or lower(u.email) like :keyword";
        Long total = entityManager.createQuery(countJpql, Long.class)
            .setParameter("keyword", "%"+keyword.toLowerCase() + "%")
            .getSingleResult();
        return new PageImpl<>(results, pageable, total);
    }
}
