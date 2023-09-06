package com.ancho.crud.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class)    //  입력한 테스트만 컨트롤러 테스트 가능하게 함 
class ArticleControllerTest {

    private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {  //  test에 있는 생성자가 하나라면 반드시 @Autowired 선언 해야 함
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) //  정상 호출
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  //  데이터 확인
                .andExpect(view().name("articles/index"))   //  뷰 이름 검사
                .andExpect(model().attributeExists("articles"));    // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
    }

//    @Disabled("구현 중")   //  Disabled로 구현 중인 테스트를 실행하지 않도록 만들 수 있음
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk()) //  정상 호출
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  //  데이터 확인
                .andExpect(view().name("articles/detail"))   //  뷰 이름 검사
                .andExpect(model().attributeExists("article"))    // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
                .andExpect(model().attributeExists("articleComments"));    // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
    }

    @Disabled("구현 중")   //  Disabled로 구현 중인 테스트를 실행하지 않도록 만들 수 있음
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk()) //  정상 호출
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  //  데이터 확인
                .andExpect(view().name("articles/search"));  //  뷰 이름 검사
    }

    @Disabled("구현 중")   //  Disabled로 구현 중인 테스트를 실행하지 않도록 만들 수 있음
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        //  Given

        //  When & Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) //  정상 호출
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  //  데이터 확인
                .andExpect(view().name("articles/search-hashtag"));   //  뷰 이름 검사
    }
}
