package com.example.dulumi.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class TitleSearchService {
    private final NoticeSearchRepository noticeSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    public TitleSearchService(NoticeSearchRepository noticeSearchRepository, ElasticsearchClient elasticsearchClient) {
        this.noticeSearchRepository = noticeSearchRepository;
        this.elasticsearchClient = elasticsearchClient;
    }

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
                    .peek(hit -> System.out.println("📦 source 확인: " + hit.source()))
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("엘라스틱 검색 중 오류 발생", e);
        }
    }

    public void saveToElastic(String content, String author) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
        ElasticEntity elastic = new  ElasticEntity();
        elastic.setId(UUID.randomUUID().toString());
        elastic.setContent(content);
        elastic.setCreatedDate(Instant.now());
        elastic.setAuthor(author);

        noticeSearchRepository.save(elastic);
    }


    public void printAllContents() {
        Iterable<ElasticEntity> allIterable = noticeSearchRepository.findAll();
        List<ElasticEntity> all = StreamSupport.stream(allIterable.spliterator(), false)
                .toList();
        all.forEach(e -> System.out.println(e.getContent()));
    }
}
