package com.example.dulumi.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface NoticeSearchRepository extends ElasticsearchRepository<ElasticEntity, Long> {
    List<ElasticEntity> findByTitle(String keyword);
}
