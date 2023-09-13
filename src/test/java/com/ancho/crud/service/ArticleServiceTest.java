package com.ancho.crud.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import com.ancho.crud.domain.Article;
import com.ancho.crud.domain.UserAccount;
import com.ancho.crud.domain.type.SearchType;
import com.ancho.crud.dto.ArticleDto;
import com.ancho.crud.dto.ArticleWithCommentsDto;
import com.ancho.crud.dto.UserAccountDto;
import com.ancho.crud.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository articleRepository;

    //    정렬 기능
//    검색어 없이 검색한 경우
    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
        // When
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);
        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    //    검색어 입력한 다음 검색한 경우
    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")   //  게시판 검색 기능 + 페이지네이션
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage(){
        //  Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());
        //  When
//        List로 반환하면 그냥 검색만 가능하지만 Page로 반환하면 페이지네이션이 가능해짐
//        List<ArticleDto> articles =  sut.searchArticles(SearchType.TITLE, "search keyword");   //  제목, 본문, ID, 닉네임, 해시태그
        Page<ArticleDto> articles =  sut.searchArticles(searchType, searchKeyword, pageable);//  제목, 본문, ID, 닉네임, 해시태그

        //  Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")   //  각 게시글 페이지로 이동
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle(){
        //  Given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        // When
        ArticleWithCommentsDto dto = sut.getArticle(articleId);
        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title",article.getTitle())
                .hasFieldOrPropertyWithValue("content",article.getContent())
                .hasFieldOrPropertyWithValue("hashtag",article.getHashtag());
        then(articleRepository).should().findById(articleId);

    }

    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException(){
        // Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());
        // When
        Throwable t = catchThrowable(() -> sut.getArticle(articleId));
        // Then
        assertThat(t).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(("게시글이 없습니다 - articleId:" + articleId));
        then(articleRepository).should().findById(articleId);
    }


    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){
        // Given
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());
        // When
        sut.saveArticle(dto);
        // Then
        then(articleRepository).should().save(any(Article.class)); // save 메소드가 호출되었는지 여부를 확인
    }


    @DisplayName("게시글의 수정정보를 입력하면 게시글을 수정한다.")
    @Test
    void givenAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle(){
        // Given
        Article article = createArticle();

        ArticleDto dto = createArticleDto("새 타이틀","새 내용","#springboot");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);

        // When
        sut.updateArticle(dto);
        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title",dto.title())
                .hasFieldOrPropertyWithValue("content",dto.content())
                .hasFieldOrPropertyWithValue("hashtag",dto.hashtag());
        then(articleRepository).should().getReferenceById(dto.id());
    }
    @DisplayName("없는 게시글의 수정정보를 입력하면 경고 로그를 찍고 아무것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing(){
        // Given
        ArticleDto dto = createArticleDto("새 타이틀","새 내용","#springboot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto);
        // Then

        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글 ID를 입력하면 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle(){
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleRepository).deleteById(articleId);

        // When
        sut.deleteArticle(1L);
        // Then

        then(articleRepository).should().deleteById(articleId); // delete 메소드가 호출되었는지 여부를 확인
    }

    private UserAccount createUserAccount(){
        return UserAccount.of(
                "ancho",
                "password",
                "ancho@email.com",
                "Ancho",
                null
        );
    }
    private Article createArticle(){
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title","content","#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                LocalDateTime.now(),
                "Ancho",
                LocalDateTime.now(),
                "Ancho"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "ancho",
                "password",
                "ancho@mail.com",
                "Ancho",
                "This is memo",
                LocalDateTime.now(),
                "ancho",
                LocalDateTime.now(),
                "ancho"
        );
    }
}
