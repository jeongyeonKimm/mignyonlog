package com.mignyon.mignyonlog.api.service.post;

import com.mignyon.mignyonlog.domain.post.Post;
import com.mignyon.mignyonlog.domain.post.PostRepository;
import com.mignyon.mignyonlog.domain.user.User;
import com.mignyon.mignyonlog.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        Post savedPost = postService.createPost(1L, "장꾸를 소개합니다", "장꾸는 바보~", "12345");

        // then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("장꾸를 소개합니다");
        assertThat(savedPost.getContent()).isEqualTo("장꾸는 바보~");
        assertThat(savedPost.getUser().getName()).isEqualTo("mignyon");

        verify(postRepository, times(1)).save(any(Post.class));
    }
}
