package com.example.dulumi.elasticsearch;

import com.example.dulumi.Repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleSearchService {
    private final NoticeSearchRepository noticeSearchRepository;

    public ElasticEntity createNotice(ElasticEntity elasticEntity) {
        return noticeSearchRepository.save(elasticEntity);
    }

    public List<ElasticEntity> getTitle(String keyword) {
        List<ElasticEntity> byTitle = noticeSearchRepository.findByTitle(keyword);
        return byTitle;
    }
}
