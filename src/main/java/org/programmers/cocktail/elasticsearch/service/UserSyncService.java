package org.programmers.cocktail.elasticsearch.service;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.programmers.cocktail.elasticsearch.elasticsearch.ElasticsearchClientComponent;
import org.programmers.cocktail.elasticsearch.dto.ElasticsearchRequest;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.CocktailLikes;
import org.programmers.cocktail.entity.Comments;
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepository;
import org.programmers.cocktail.repository.comments.CommentsRepository;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserSyncService {

    private final ElasticsearchClientComponent elasticsearchClientComponent;
    private final UsersRepository usersRepository;
    private final CocktailsRepository cocktailsRepository;
    private final CocktailLikesRepository cocktailLikesRepository;
    private final CommentsRepository commentsRepository;

    private LocalDateTime lastSyncTime;

    public UserSyncService(
        ElasticsearchClientComponent elasticsearchClientComponent,
        UsersRepository usersRepository,
        CocktailsRepository cocktailsRepository,
        CocktailLikesRepository cocktailLikesRepository,
        CommentsRepository commentsRepository
    ) {
        this.elasticsearchClientComponent = elasticsearchClientComponent;
        this.usersRepository = usersRepository;
        this.cocktailsRepository = cocktailsRepository;
        this.cocktailLikesRepository = cocktailLikesRepository;
        this.commentsRepository = commentsRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void syncToElasticsearch() {
        try {

            if (lastSyncTime == null) {
                lastSyncTime = findMostRecentUpdatedAt();// 기본값 설정
            }
            // 좋아요 데이터 동기화
            syncLikes();
            // 댓글 데이터 동기화
            syncComments();

            lastSyncTime = LocalDateTime.now();
            log.info("Successfully synced all data to Elasticsearch.");
        } catch (Exception e) {
            log.error("Failed to sync data to Elasticsearch", e);
            throw new RuntimeException("Failed to sync data to Elasticsearch", e);
        }
    }

    private void syncLikes() throws Exception {
        log.info("Starting to sync likes...");
        List<CocktailLikes> likes = cocktailLikesRepository.findByUpdatedAtAfter(lastSyncTime);
        log.info("Found {} likes to sync.", likes.size());

        if (likes.isEmpty()) {
            return;
        }

        int batchSize = 10000; // 배치 크기 설정
        for (int i = 0; i < likes.size(); i += batchSize) {
            List<CocktailLikes> batch = likes.subList(i, Math.min(i + batchSize, likes.size()));

            BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();
            for (CocktailLikes like : batch) {
                log.debug("Processing like for user ID: {}, cocktail ID: {}", like.getUsers().getId(), like.getCocktails().getId());
                ElasticsearchRequest request = convertLikeToDTO(like);
                bulkRequestBuilder.operations(op -> op
                    .index(idx -> idx
                        .index("cocktail_interactions")
                        .id(like.getUsers().getId() + "_" + like.getCocktails().getId() + "_like")
                        .document(request)
                    )
                );
            }

            log.info("Executing bulk request for batch {} to {}...", i, Math.min(i + batchSize, likes.size()));
            executeBulkRequest(bulkRequestBuilder.build());
            log.info("Finished bulk request for batch {} to {}.", i, Math.min(i + batchSize, likes.size()));
        }

        log.info("Finished syncing all likes.");
    }


    private void syncComments() throws Exception {
        log.info("Starting to sync comments...");
        List<Comments> comments = commentsRepository.findByUpdatedAtAfter(lastSyncTime);
        log.info("Found {} comments to sync.", comments.size());

        if (comments.isEmpty()) {
            return;
        }

        int batchSize = 10000; // 배치 크기 설정
        for (int i = 0; i < comments.size(); i += batchSize) {
            List<Comments> batch = comments.subList(i, Math.min(i + batchSize, comments.size()));

            BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();
            for (Comments comment : batch) {
                log.debug("Processing comment for user ID: {}, cocktail ID: {}, comment ID: {}",
                    comment.getUsers().getId(), comment.getCocktails().getId(), comment.getId());

                ElasticsearchRequest request = convertCommentToDTO(comment);
                bulkRequestBuilder.operations(op -> op
                    .index(idx -> idx
                        .index("cocktail_interactions")
                        .id(comment.getUsers().getId() + "_" + comment.getCocktails().getId() + "_comment_" + comment.getId())
                        .document(request)
                    )
                );
            }

            log.info("Executing bulk request for batch starting at index {}...", i);
            executeBulkRequest(bulkRequestBuilder.build());
        }

        log.info("Finished syncing comments.");
    }


    private void executeBulkRequest(BulkRequest bulkRequest) throws Exception {
        BulkResponse response = elasticsearchClientComponent.getClient().bulk(bulkRequest);
        if (response.errors()) {
            response.items().forEach(item -> {
                if (item.error() != null) {
                    log.error("Error syncing document with ID {}: {}",
                        item.id(), item.error().reason());
                }
            });
            throw new RuntimeException("Some documents failed to sync.");
        }
    }

    private ElasticsearchRequest convertLikeToDTO(CocktailLikes like) {
        Users user = like.getUsers();
        Cocktails cocktail = like.getCocktails();

        return ElasticsearchRequest.builder()
            .userId(user.getId())
            .age(user.getAge())
            .gender(user.getGender())
            .cocktailId(cocktail.getId())
            .cocktailName(cocktail.getName())
            .category(cocktail.getCategory())
            .ingredients(cocktail.getIngredients())
            .interactionType("like")
            .timestamp(like.getCreatedAt().toString())
            .build();
    }

    private ElasticsearchRequest convertCommentToDTO(Comments comment) {
        Users user = comment.getUsers();
        Cocktails cocktail = comment.getCocktails();

        return ElasticsearchRequest.builder()
            .userId(user.getId())
            .age(user.getAge())
            .gender(user.getGender())
            .cocktailId(cocktail.getId())
            .cocktailName(cocktail.getName())
            .category(cocktail.getCategory())
            .ingredients(cocktail.getIngredients())
            .interactionType("comment")
            .commentContent(comment.getContent())
            .timestamp(comment.getCreatedAt().toString())
            .build();
    }

    private LocalDateTime findMostRecentUpdatedAt() {
        LocalDateTime mostRecentLikes = cocktailLikesRepository.findMostRecentUpdatedAt();
        LocalDateTime mostRecentComments = commentsRepository.findMostRecentUpdatedAt();
        return mostRecentLikes.isAfter(mostRecentComments) ? mostRecentLikes : mostRecentComments;
    }
}