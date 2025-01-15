package org.programmers.cocktail.elasticsearch.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ElasticsearchClientComponent {

    private final ElasticsearchClient elasticsearchClient;


    public ElasticsearchClientComponent() {
        String elasticIp = System.getenv("ELASTIC_IP");
        int elasticPort = Integer.parseInt(System.getenv("ELASTIC_PORT"));
        RestClient restClient = RestClient.builder(
            new HttpHost(elasticIp, elasticPort, "http")
        ).build();

        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        this.elasticsearchClient = new ElasticsearchClient(transport);
    }

    public ElasticsearchClient getClient() {
        return elasticsearchClient;
    }
}
