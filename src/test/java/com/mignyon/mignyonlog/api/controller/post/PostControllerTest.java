package com.mignyon.mignyonlog.api.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mignyon.mignyonlog.api.controller.post.request.PostCreateRequest;
import com.mignyon.mignyonlog.api.service.post.PostService;
import com.mignyon.mignyonlog.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.mignyon.mignyonlog.common.constant.PostConstants.USER_ID_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("신규 게시글을 등록한다.")
    @Test
    void createPost() throws Exception {
        // given
        PostCreateRequest request = getPostCreateRequest();

        // when
        mockMvc.perform(
                        post("/api/posts")
                                .header(USER_ID_HEADER, 1L)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        // then

    }

    @DisplayName("사용자 아이디가 헤더에 없으면 신규 게시글을 등록에 실패한다.")
    @Test
    void createPostWithoutHeader() throws Exception {
        // given
        PostCreateRequest request = getPostCreateRequest();

        // when
        mockMvc.perform(post("/api/posts")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("신규 게시글을 등록시 제목은 필수값이다.")
    @Test
    void createPostWithoutTitle() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .content("바보옹")
                .password("12345")
                .build();

        // when
        mockMvc.perform(post("/api/posts")
                        .header(USER_ID_HEADER, 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").value("제목은 필수입니다."))
                .andDo(print());
    }

    @DisplayName("신규 게시글을 등록시 비밀번호는 필수값이다.")
    @Test
    void createPostWithoutPassword() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("장꾸꾸")
                .content("바보옹")
                .build();

        // when
        mockMvc.perform(post("/api/posts")
                        .header(USER_ID_HEADER, 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andExpect(jsonPath("$.errors[0].message").value("비밀번호는 필수입니다."))
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
