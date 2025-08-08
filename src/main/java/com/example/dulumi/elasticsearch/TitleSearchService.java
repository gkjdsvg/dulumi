package com.example.dulumi.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    public List<ElasticEntity> search(String keyword) {
        System.out.println("🔥 search() 진입");
        Query query = Query.of(q -> q
                .match(m -> m
                        .field("content")
                        .query(keyword))
        );

        SearchRequest request = SearchRequest.of(s -> s
                .index("my_nori_index_v2")
                .query(query)
        );

        try {
            // 1. Map으로 받기
            SearchResponse<Map> mapResponse = elasticsearchClient.search(request, Map.class);
            System.out.println("==== ES 원본 데이터 ====");
            mapResponse.hits().hits().forEach(hit -> System.out.println(hit.source()));

            // 2. 기존 매핑 버전
            SearchResponse<ElasticEntity> response = elasticsearchClient.search(request, ElasticEntity.class);
            System.out.println("==== ElasticEntity 매핑 데이터 ====");
            response.hits().hits().forEach(hit -> System.out.println(hit.source()));

            return response.hits().hits().stream()
                    .peek(hit -> System.out.println("📦 source 확인: " + hit.source()))
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("엘라스틱 검색 중 오류 발생", e);
        }
    }

    //과거 버전
//    public List<ElasticEntity> search(String keyword) throws JsonProcessingException {
//        org.elasticsearch.action.search.SearchRequest searchRequest = new org.elasticsearch.action.search.SearchRequest("my_nori_index_v2");
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchQuery("content", keyword));
//        searchRequest.source(searchSourceBuilder);
//
//        org.elasticsearch.action.search.SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
//
//        List<ElasticEntity> results = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//
//        for (SearchHit hit : response.getHits().getHits()) {
//            String sourceJson = hit.getSourceAsString();
//            ElasticEntity entity = mapper.readValue(sourceJson, ElasticEntity.class);
//            results.add(entity);
//        }
//
//        return results;
//    }

    public void saveToElastic(String content, String author) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
        ElasticEntity elastic = new  ElasticEntity();
        elastic.setId(UUID.randomUUID().toString());
        elastic.setContent(content);
        elastic.setCreatedDate(LocalDate.now());
        elastic.setAuthor(author);

        System.out.println("🔥 저장 시도 : " + elastic);

        try {
            noticeSearchRepository.save(elastic);
            System.out.println("✅ 엘라스틱 저장 성공!");
        }catch(Exception e) {
            System.out.println("❌ 엘라스틱 저장 실패 : " + e.getMessage());
        }

    }


    public void printAllContents() {
        Iterable<ElasticEntity> allIterable = noticeSearchRepository.findAll();
        List<ElasticEntity> all = StreamSupport.stream(allIterable.spliterator(), false)
                .toList();
        all.forEach(e -> System.out.println(e.getContent()));
    }
}
