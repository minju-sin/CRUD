package com.ancho.crud.service;

import com.ancho.crud.domain.Article;
import com.ancho.crud.domain.ArticleComment;
import com.ancho.crud.domain.UserAccount;
import com.ancho.crud.domain.type.SearchType;
import com.ancho.crud.dto.ArticleCommentDto;
import com.ancho.crud.dto.ArticleDto;
import com.ancho.crud.dto.UserAccountDto;
import com.ancho.crud.repository.ArticleCommentRepository;
import com.ancho.crud.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @Mock
    private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;
    @InjectMocks private ArticleCommentService sut;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments(){
        //  Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
        //  When
        List<ArticleCommentDto> actual =  sut.searchArticleComment(articleId);   //  ID

        //  Then
        assertThat(actual).hasSize(1)
                        .first().hasFieldOrPropertyWithValue("content",expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment(){
        //  Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");

        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());

        //  When
        sut.saveArticleComment(dto);

        //  Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));

    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        // When
        sut.saveArticleComment(dto);
        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
    @Test
    void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComment(){
        // Given
        String oldContent = "content";
        String updatedContent = "댓글";
        ArticleComment articleComment = createArticleComment(oldContent);
        ArticleCommentDto dto = createArticleCommentDto(updatedContent);
        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);
        // When
        sut.saveArticleComment(dto);
        // Then
        assertThat(articleComment.getContent())
                .isNotEqualTo(oldContent)
                .isEqualTo(updatedContent);
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무것도 안한다.")
    @Test
    void giveNonexistentArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);
        // When
        sut.updateArticleComment(dto);
        // Then
        then(articleCommentRepository).should().getReferenceById(dto.id());

    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void giveArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment(){
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);
        // When
        sut.deleteArticleComment(articleCommentId);
        // Then
        then(articleCommentRepository).should().deleteById(articleCommentId);

    }



    private ArticleComment createArticleComment(String content) {
        return ArticleComment.of(
                Article.of(createUserAccount(),"title","content","hashtag"),
                createUserAccount(),
                content
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "jyc",
                "password",
                "jyc@email.com",
                "Jyc",
                null
        );
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
                LocalDateTime.now(),
                "jyc",
                LocalDateTime.now(),
                "jyc"
        );

    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "Ancho",
                "password",
                "ancho@mail.com",
                "Ancho",
                "This is memo",
                LocalDateTime.now(),
                "Ancho",
                LocalDateTime.now(),
                "Ancho"
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }
}