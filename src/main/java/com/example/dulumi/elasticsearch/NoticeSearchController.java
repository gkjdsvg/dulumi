package com.example.dulumi.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("api/notice/search")
public class NoticeSearchController {
    private final  TitleSearchService titleSearchService;

    @GetMapping
    public ApiResponse<List<ElasticEntity>> search(@RequestParam("keyword") String keyword) {
        return ApiResponse.onSuccess(titleSearchService.getTitle(keyword));
    }

    @PostMapping
    public ApiResponse<ElasticEntity> create(@RequestBody ElasticEntity elasticEntity) {
        ElasticEntity saved = titleSearchService.createNotice(elasticEntity);
        return ApiResponse.onSuccess(saved);
    }
}
