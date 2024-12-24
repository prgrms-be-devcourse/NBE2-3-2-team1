package org.programmers.cocktail.repository.comments;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.entity.QComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CommentsRepositoryImpl implements CommentsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;


    public CommentsRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countTotalCommentsUntilYesterday(LocalDateTime today) {
        QComments comments = QComments.comments;

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);

        return queryFactory
            .select(comments.count())
            .from(comments)
            .where(
                comments.updatedAt.loe(yesterday)
            )
            .fetchOne();
    }

    @Override
    public List<Long> countCommentsList(LocalDateTime today) {
        QComments comments = QComments.comments;

        List<Long> lists = new ArrayList<>();
        for (int i=0; i<7; i++) {
            lists.add(queryFactory
                .select(comments.count())
                .from(comments)
                .where(
                    comments.updatedAt.loe(today
                        .minusDays(6)
                        .withHour(23)
                        .withMinute(59)
                        .withSecond(59)
                        .plusDays(i))
                )
                .fetchOne());
        }

        return lists;
    }

    @Override
    public Page<Comments> searchByKeyword(String keyword, Pageable pageable) {
        String jpql = "select c from comments c where lower(c.content) like :keyword";

        List<Comments> comments = entityManager.createQuery(jpql, Comments.class)
            .setParameter("keyword", "%" + keyword.toLowerCase() + "%")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();


        String countJpql = "SELECT COUNT(c) FROM comments c WHERE LOWER(c.content) LIKE :keyword";
        Long total = entityManager.createQuery(countJpql, Long.class)
            .setParameter("keyword", "%"+keyword.toLowerCase() + "%")
            .getSingleResult();

        return new PageImpl<>(comments, pageable, total);
    }
}
