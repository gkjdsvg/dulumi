//package com.example.dulumi.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.elasticsearch.client.RestClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ElasticSearchClientConfig {
//    @Bean
//    public ElasticsearchClient elasticsearchClient(RestClient restClient, ObjectMapper objectMapper) {
//        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper(objectMapper);
//        ElasticsearchTransport transport = new RestClientTransport(restClient, jsonpMapper);
//        return new ElasticsearchClient(transport);
//    }
//}
