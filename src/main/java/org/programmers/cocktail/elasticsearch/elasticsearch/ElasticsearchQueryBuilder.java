package org.programmers.cocktail.elasticsearch.elasticsearch;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchQueryBuilder {

    private final ElasticsearchClientComponent elasticsearchClientComponent;
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchQueryBuilder.class);


    public ElasticsearchQueryBuilder(ElasticsearchClientComponent elasticsearchClientComponent) {
        this.elasticsearchClientComponent = elasticsearchClientComponent;
    }

    public Map<String, Long> fetchWordCloudData(String gender, String ageGroup) throws IOException {
        int ageStart = Integer.parseInt(ageGroup);
        int ageEnd = ageStart + 9;

        log.info("Fetching word cloud data");

        SearchResponse<Void> response = elasticsearchClientComponent.getClient().search(s -> s
                .index("cocktail_interactions") // Elasticsearch 인덱스
                .size(0) // 문서 제외, 집계만 반환
                .query(q -> q.bool(b -> b
                    .filter(f -> f.term(t -> t
                        .field("gender.keyword") // 성별 필드
                        .value(gender) // 성별 값
                    ))
                    .filter(f -> f.range(r -> r
                        .number(n -> n
                            .field("age") // 연령 필드
                            .gte((double) ageStart) // 시작 연령
                            .lte((double) ageEnd) // 종료 연령
                        )
                    ))
                ))
                .aggregations("wordcloud", a -> a
                    .terms(t -> t
                        .field("cocktailName.keyword") // 댓글 키워드
                        .size(100) // 상위 100개
                    )
                ),
            Void.class);

        log.info("Search response received. Parsing results...");

        Map<String, Long> wordCloudData = new HashMap<>();
        StringTermsAggregate wordCloudAgg = response.aggregations().get("wordcloud").sterms();

        for (StringTermsBucket bucket : wordCloudAgg.buckets().array()) {
            String key = bucket.key().stringValue();
            long docCount = bucket.docCount();
            wordCloudData.put(key, docCount);

            log.debug("Word: {}, Count: {}", key, docCount);
        }

        log.info("Word cloud data fetching completed. Total words: {}", wordCloudData.size());
        return wordCloudData;
    }
}
