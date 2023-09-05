package com.ancho.crud.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DisplayName("Data Rest - API 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[API] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result ->  content().contentType(MediaType.valueOf("application/hal+json")));
//                .andDo(print());  상세 내용 보여주는 코드
    }


    @DisplayName("[API] 게시글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/api/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result ->  content().contentType(MediaType.valueOf("application/hal+json")));
    }


    @DisplayName("[API] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnArticleCommentsJsonResponse() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result ->  content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[API] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/api/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result ->  content().contentType(MediaType.valueOf("application/hal+json")));
    }


    @DisplayName("[API] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result ->  content().contentType(MediaType.valueOf("application/hal+json")));
    }

}
