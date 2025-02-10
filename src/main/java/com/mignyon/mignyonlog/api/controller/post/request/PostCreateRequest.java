package com.mignyon.mignyonlog.api.controller.post.request;

import com.mignyon.mignyonlog.api.service.post.request.PostCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String content;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    private PostCreateRequest(Long userId, String title, String content, String password) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.password = password;
    }

    public PostCreateServiceRequest toServiceRequest() {
        return PostCreateServiceRequest.builder()
                .userId(this.userId)
                .title(this.title)
                .content(this.content)
                .password(this.password)
                .build();
    }
}
