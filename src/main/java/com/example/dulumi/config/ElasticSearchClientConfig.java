//package com.example.dulumi.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ElasticSearchClientConfig {
//
//    private final ObjectMapper objectMapper;
//
//    public ElasticSearchClientConfig(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper(objectMapper);
//
//        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "https")).build();
//
//        RestClientTransport transport = new RestClientTransport(restClient, jsonpMapper);
//
//        return new  ElasticsearchClient(transport);
//    }
//}
