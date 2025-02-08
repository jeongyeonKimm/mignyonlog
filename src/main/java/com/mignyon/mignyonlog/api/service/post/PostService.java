package com.mignyon.mignyonlog.api.service.post;

import com.mignyon.mignyonlog.domain.post.Post;
import com.mignyon.mignyonlog.domain.post.PostRepository;
import com.mignyon.mignyonlog.domain.user.User;
import com.mignyon.mignyonlog.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Post createPost(Long userId, String title, String content, String password) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        final Post post = Post.builder()
                .title(title)
                .content(content)
                .password(password)
                .user(user)
                .build();

        return postRepository.save(post);
    }
}
