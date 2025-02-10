package com.mignyon.mignyonlog.api.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mignyon.mignyonlog.api.controller.post.request.PostCreateRequest;
import com.mignyon.mignyonlog.api.service.post.PostService;
import com.mignyon.mignyonlog.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

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
        PostCreateRequest request = getPostCreateRequest("장꾸꾸", "바보옹");

        // when
        mockMvc.perform(
                        post("/api/posts")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        // then

    }

    @DisplayName("신규 게시글을 등록시 제목, 비밀번호는 필수값이다.")
    @ParameterizedTest
    @MethodSource("invalidPostCreateParameter")
    void createPostWithoutTitle(String title, String password, String expectedErrorField, String expectedErrorMessage) throws Exception {
        // given
        PostCreateRequest request = getPostCreateRequest(title, password);

        // when
        mockMvc.perform(post("/api/posts")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errors[0].field").value(expectedErrorField))
                .andExpect(jsonPath("$.errors[0].message").value(expectedErrorMessage))
                .andDo(print());
    }

    private PostCreateRequest getPostCreateRequest(String title, String password) {
        return PostCreateRequest.builder()
                .userId(1L)
                .title(title)
                .content("바보옹")
                .password(password)
                .build();
    }

    private static Stream<Arguments> invalidPostCreateParameter() {
        return Stream.of(
                Arguments.of(null, "12345", "title", "제목은 필수입니다."),
                Arguments.of("장꾸꾸", null, "password", "비밀번호는 필수입니다.")
        );
    }
}
