package com.mignyon.mignyonlog.api.service.post;

import com.mignyon.mignyonlog.api.service.post.request.PostCreateServiceRequest;
import com.mignyon.mignyonlog.api.service.post.response.PostResponse;
import com.mignyon.mignyonlog.domain.post.Post;
import com.mignyon.mignyonlog.domain.post.PostRepository;
import com.mignyon.mignyonlog.domain.user.User;
import com.mignyon.mignyonlog.domain.user.UserRepository;
import com.mignyon.mignyonlog.exception.RestApiException;
import com.mignyon.mignyonlog.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostResponse createPost(final PostCreateServiceRequest request) {
        final User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER));

        final Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .password(request.getPassword())
                .user(user)
                .build();

        final Post savedPost = postRepository.save(post);

        return PostResponse.of(post);
    }
}
