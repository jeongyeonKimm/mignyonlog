package com.mignyon.mignyonlog.api.service.post.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateServiceRequest {

    private Long userId;
    private String title;
    private String content;
    private String password;

    @Builder
    private PostCreateServiceRequest(Long userId, String title, String content, String password) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.password = password;
    }
}
