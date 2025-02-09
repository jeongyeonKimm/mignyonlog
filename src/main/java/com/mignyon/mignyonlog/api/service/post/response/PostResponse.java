package com.mignyon.mignyonlog.api.service.post.response;

import com.mignyon.mignyonlog.domain.post.Post;
import com.mignyon.mignyonlog.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private User user;

    @Builder
    private PostResponse(Long id, String title, String content, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static PostResponse of(final Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(post.getUser())
                .build();
    }
}
