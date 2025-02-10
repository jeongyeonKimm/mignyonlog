package com.mignyon.mignyonlog.api.controller.post.request;

import com.mignyon.mignyonlog.api.service.post.request.PostCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String content;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    private PostCreateRequest(String title, String content, String password) {
        this.title = title;
        this.content = content;
        this.password = password;
    }

    public PostCreateServiceRequest toServiceRequest(Long userId) {
        return PostCreateServiceRequest.builder()
                .userId(userId)
                .title(this.title)
                .content(this.content)
                .password(this.password)
                .build();
    }
}
