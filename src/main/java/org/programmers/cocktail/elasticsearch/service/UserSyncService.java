package org.programmers.cocktail.elasticsearch.service;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.programmers.cocktail.elasticsearch.component.ElasticsearchClientComponent;
import org.programmers.cocktail.elasticsearch.dto.ElasticUserRequest;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.repository.users.UsersRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserSyncService {

    private final ElasticsearchClientComponent elasticsearchClientComponent;
    private final UsersRepository usersRepository;

    private LocalDateTime lastSyncTime = LocalDateTime.MIN;

    public UserSyncService(ElasticsearchClientComponent elasticsearchClientComponent,
        UsersRepository usersRepository) {
        this.elasticsearchClientComponent = elasticsearchClientComponent;
        this.usersRepository = usersRepository;
    }

    @Scheduled(fixedRate = 3600000)
    public void syncUsersToElasticsearch() {
        try {
            List<Users> usersEntities = usersRepository.findUpdatedSince(lastSyncTime);

            List<ElasticUserRequest> users = usersEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());



            BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

            for (ElasticUserRequest user : users) {
                bulkRequestBuilder.operations(op -> op
                    .index(idx -> idx
                        .index("users_index")
                        .id(String.valueOf(user.getId()))
                        .document(user)
                    )
                );
            }

            BulkRequest bulkRequest = bulkRequestBuilder.build();

            BulkResponse response = elasticsearchClientComponent.getClient().bulk(bulkRequest);
            if (response.errors()) {
                response.items().forEach(item -> {
                    if (item.error() != null) {
                        System.err.println(
                            "Error syncing user with ID " + item.id() + ": " + item.error()
                                .reason());
                    }
                });
                throw new RuntimeException("Some users failed to sync.");
            }

            lastSyncTime = LocalDateTime.now();

            System.out.println("Successfully synced all users to Elasticsearch.");
        } catch (Exception e) {
            System.out.println("Some users failed to sync.");
            throw new RuntimeException("Failed to sync users to Elasticsearch", e);
        }
    }

    private ElasticUserRequest convertToDTO(Users user) {
        return ElasticUserRequest.builder()
            .id(user.getId())
            .age(user.getAge())
            .gender(user.getGender())
            .build();
    }

}
