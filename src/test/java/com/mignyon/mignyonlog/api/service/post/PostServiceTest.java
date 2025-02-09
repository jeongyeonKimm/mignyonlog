package com.mignyon.mignyonlog.api.service.post;

import com.mignyon.mignyonlog.api.service.post.request.PostCreateServiceRequest;
import com.mignyon.mignyonlog.api.service.post.response.PostResponse;
import com.mignyon.mignyonlog.domain.post.Post;
import com.mignyon.mignyonlog.domain.post.PostRepository;
import com.mignyon.mignyonlog.domain.user.User;
import com.mignyon.mignyonlog.domain.user.UserRepository;
import com.mignyon.mignyonlog.exception.RestApiException;
import com.mignyon.mignyonlog.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @DisplayName("게시글 제목, 내용, 비밀번호, 유저를 받아 게시글을 저장한다.")
    @Test
    void createPost() {
        // given
        final User user = User.builder()
                .name("mignyon")
                .build();

        final Post post = Post.builder()
                .title("장꾸를 소개합니다")
                .content("장꾸는 바보~")
                .password("12345")
                .user(user)
                .build();

        final PostCreateServiceRequest request = PostCreateServiceRequest.builder()
                .userId(1L)
                .title("장꾸를 소개합니다")
                .content("장꾸는 바보~")
                .password("12345")
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        PostResponse savedPost = postService.createPost(request);

        // then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("장꾸를 소개합니다");
        assertThat(savedPost.getContent()).isEqualTo("장꾸는 바보~");
        assertThat(savedPost.getUser().getName()).isEqualTo("mignyon");

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @DisplayName("존재하지 않는 유저는 게시글 저장에 실패한다.")
    @Test
    void createPostWithInvalidUser() {
        // given
        final PostCreateServiceRequest request = PostCreateServiceRequest.builder()
                .userId(999L)
                .title("장꾸를 소개합니다")
                .content("장꾸는 바보~")
                .password("12345")
                .build();

        given(userRepository.findById(999L)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> postService.createPost(request))
                .isInstanceOf(RestApiException.class)
                .hasMessage(UserErrorCode.INVALID_USER.getMessage());

        // then
        then(userRepository).should(times(1)).findById(999L);
        then(postRepository).should(never()).save(any(Post.class));
    }
}
