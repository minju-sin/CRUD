package com.ancho.crud.repository;

import static org.assertj.core.api.Assertions.*;
import com.ancho.crud.config.JpaConfig;
import com.ancho.crud.domain.Article;
import com.ancho.crud.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)    //  JpaConfig 는 내가 직접 만든 것으로 import 함
@DataJpaTest
class JpaRepositoryTest {
    //  생성자 주입 패턴
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    private final UserAccountRepository userAccountRepository;
    JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository= userAccountRepository;
    }

    //    articleRepostory를 findAll 을 사용하여 모든 내용을 List로 가져오는 테스트를 진행한다.
    @DisplayName("Select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
        //  Given

        //  When
        List<Article> articles = articleRepository.findAll();


        //  Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }



    //    articleRepository의 갯수previousCount로 선언, 새로운 article 엔티티를 생성후 저장한 다음,
//    articleRepository의 갯수가 previousCount에서 1을 더한 값과 같으면 테스트 성공이다.
    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine(){
        //  Given
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of(
                "Ancho","pw",null,null,null
        ));

        Article article = Article.of(userAccount,"new Article", "new content", "#spring");
        //  When
        articleRepository.save(article);


        //  Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }


    //    기존의 데이터를 수정했을 때 쿼리 발생하는지 확인
    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        //  Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        //  When
        Article savedArticle = articleRepository.saveAndFlush(article);

        //  Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }


    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        //  Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();  //  기존의 게시글 개수
        long previousArticleComment = articleCommentRepository.count(); //  기존의 댓글 개수
        //  게시글을 삭제할 때, 해당 게시글의 댓글 사이즈 확인
        int deletedCommentSize = article.getArticleComments().size();

        //  When
        articleRepository.delete(article);  //  delete

        //  Then
//        게시글 삭제를 하면 기존 게시글 개수에서 1개씩 사라짐
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
//        댓글 삭제할 땐, 기존의 댓글 개수에서 게시글이 삭제될 때 전체 댓글을 지우는 것도 포함해서 생각함
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleComment - deletedCommentSize);
    }
}
