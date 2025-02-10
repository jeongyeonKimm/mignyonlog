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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.mignyon.mignyonlog.common.constant.PostConstants.USER_ID_HEADER;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<PostResponse> createPost(@RequestHeader(USER_ID_HEADER) Long userId,
                                                   @RequestBody @Valid PostCreateRequest request) {

        PostResponse response = postService.createPost(request.toServiceRequest(userId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
