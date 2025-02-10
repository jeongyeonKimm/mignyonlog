package com.mignyon.mignyonlog.api.controller.post;

import com.mignyon.mignyonlog.api.controller.post.request.PostCreateRequest;
import com.mignyon.mignyonlog.api.service.post.PostService;
import com.mignyon.mignyonlog.api.service.post.response.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostCreateRequest request) {

        PostResponse response = postService.createPost(request.toServiceRequest());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
