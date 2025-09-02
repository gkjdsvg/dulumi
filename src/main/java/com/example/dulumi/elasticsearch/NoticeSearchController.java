package com.example.dulumi.elasticsearch;

import com.example.dulumi.Repository.SearchHistoryRepository;
import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.domain.PrincipalDetails;
import com.example.dulumi.domain.SearchHistory;
import com.example.dulumi.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("api/notice/search")
public class NoticeSearchController {
    private final  TitleSearchService titleSearchService;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

//    @GetMapping
//    public ResponseEntity<List<ElasticEntity>> search(@RequestParam("keyword") String keyword) { //keyword를 받아서
//        List<ElasticEntity> results = titleSearchService.search(keyword); //검색한 걸 list에 저장하는 거네
//        return ResponseEntity.ok().body(results); //그걸 반환해서 보여주는 거고
//    }

    @GetMapping
    public ResponseEntity<List<ElasticEntity>> search(@RequestParam("keyword") String keyword, Authentication authentication) {
        //로그인한 사용자 정보 가져오기
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        //검색 기록 저장
        SearchHistory history = new SearchHistory();
        history.setKeyword(keyword);
        history.setUser(user);
        history.setDate(LocalDateTime.now());
        searchHistoryRepository.save(history);

        List<ElasticEntity> results = titleSearchService.search(keyword);

        return ResponseEntity.ok(results);
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
