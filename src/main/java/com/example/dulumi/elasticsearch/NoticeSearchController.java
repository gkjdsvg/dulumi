package com.example.dulumi.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("api/notice/search")
public class NoticeSearchController {
    private final  TitleSearchService titleSearchService;

    @GetMapping
    public ResponseEntity<List<ElasticEntity>> search(@RequestParam("keyword") String keyword) { //keyword를 받아서
        List<ElasticEntity> results = titleSearchService.search(keyword); //검색한 걸 list에 저장하는 거네
        return ResponseEntity.ok().body(results); //그걸 반환해서 보여주는 거고
    }

    @PostMapping
    public ApiResponse<ElasticEntity> create(@RequestBody ElasticEntity elasticEntity) {
        ElasticEntity saved = titleSearchService.createNotice(elasticEntity);
        return ApiResponse.onSuccess(saved);
    }

    @PostMapping("/check")
    public void check() {
        titleSearchService.printAllContents();
    }

}
