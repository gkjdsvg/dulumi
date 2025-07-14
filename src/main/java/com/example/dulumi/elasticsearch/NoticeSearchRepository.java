package com.example.dulumi.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeSearchRepository extends ElasticsearchRepository<ElasticEntity, Long> {
    List<ElasticEntity> findByContent(String keyword);
}
