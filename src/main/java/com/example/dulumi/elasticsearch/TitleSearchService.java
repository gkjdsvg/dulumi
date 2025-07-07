package com.example.dulumi.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TitleSearchService {
    private final NoticeSearchRepository noticeSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    public ElasticEntity createNotice(ElasticEntity elasticEntity) {
        return noticeSearchRepository.save(elasticEntity);
    }

    public List<ElasticEntity> getContent(String keyword) {
        Query query = Query.of(q -> q
                .match(m -> m
                        .field("content")
                        .query(keyword))
        );

        SearchRequest request = SearchRequest.of(s -> s
                .index("my_nori_index")
                .query(query)
        );

        try {
            SearchResponse<ElasticEntity> response =
                    elasticsearchClient.search(request, ElasticEntity.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("엘라스틱 검색 중 오류 발생");
        }

    }

    public void printAllContents() {
        Iterable<ElasticEntity> allIterable = noticeSearchRepository.findAll();
        List<ElasticEntity> all = StreamSupport.stream(allIterable.spliterator(), false)
                .toList();
        all.forEach(e -> System.out.println(e.getContent()));
    }
}
