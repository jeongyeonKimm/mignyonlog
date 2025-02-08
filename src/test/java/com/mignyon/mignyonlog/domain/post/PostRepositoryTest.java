package com.mignyon.mignyonlog.domain.post;

import com.mignyon.mignyonlog.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @DisplayName("유저가 게시글을 생성한다.")
    @Test
    void createPost() {
        // given
        final User user = User.builder()
                .name("user1")
                .build();

        final Post post = Post.builder()
                .title("title1")
                .content("content1")
                .password("12345")
                .user(user)
                .build();

        // when
        final Post savedPost = postRepository.save(post);

        // then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("title1");
        assertThat(savedPost.getContent()).isEqualTo("content1");
        assertThat(savedPost.getPassword()).isEqualTo("12345");
        assertThat(savedPost.getUser().getName()).isEqualTo("user1");
    }
}
