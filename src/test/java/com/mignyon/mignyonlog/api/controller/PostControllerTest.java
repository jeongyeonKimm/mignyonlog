package com.mignyon.mignyonlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .build();
    }

    @DisplayName("신규 게시글을 등록한다.")
    @Test
    void createPost() {
        // given
        PostCreateRequest request = getPostCreateRequest();

        // when
        mockMvc.perform(
                        post("/api/posts")
                                .header(USER_ID_HEADER, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then

    }

    @DisplayName("사용자 아이디가 헤더에 없으면 신규 게시글을 등록에 실패한다.")
    @Test
    void createPostWithoutHeader() {
        // given
        PostCreateRequest request = getPostCreateRequest();

        // when
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());
    }

    @DisplayName("신규 게시글을 등록시 제목은 필수값이다.")
    @Test
    void createPostWithoutTitle() {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .content("바보옹")
                .password("12345")
                .build();

        // when
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("제목은 필수입니다."))
                .andDo(print());
    }

    @DisplayName("신규 게시글을 등록시 비밀번호는 필수값이다.")
    @Test
    void createPostWithoutTitle() {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("장꾸꾸")
                .content("바보옹")
                .build();

        // when
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."))
                .andDo(print());
    }

    private PostCreateRequest getPostCreateRequest() {
        return PostCreateRequest.builder()
                .title("장꾸꾸")
                .content("바보옹")
                .password("12345")
                .build();
    }
}
