package org.programmers.cocktail;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.programmers.cocktail.elasticsearch.elasticsearch.ElasticsearchClientComponent;
import org.programmers.cocktail.elasticsearch.elasticsearch.ElasticsearchQueryBuilder;
import java.util.Map;

public class ElasticsearchQueryBuilderTest {

    @Test
    void testFetchWordCloudData() throws IOException {
        // Mock ElasticsearchClientComponent
        ElasticsearchClientComponent mockClient = new ElasticsearchClientComponent();
        ElasticsearchQueryBuilder queryBuilder = new ElasticsearchQueryBuilder(mockClient);

        // Fetch word cloud data
        Map<String, Long> result = queryBuilder.fetchWordCloudData("Male", "20");

        result.forEach((word, count) -> {
            System.out.println("단어: " + word + ", 빈도수: " + count);
        });
        // Assertions
        assertNotNull(result);
        System.out.println("Fetched word cloud data: " + result);
    }

}
