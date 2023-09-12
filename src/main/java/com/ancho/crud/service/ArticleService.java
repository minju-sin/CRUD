package com.ancho.crud.service;

import com.ancho.crud.domain.Article;
import com.ancho.crud.domain.type.SearchType;
import com.ancho.crud.dto.ArticleDto;
import com.ancho.crud.dto.ArticleUpdateDto;
import com.ancho.crud.dto.ArticleWithCommentsDto;
import com.ancho.crud.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/*
* 게시판 서비스 구현
*/
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true) // 읽어주는 작업만 할 때 붙여주는 기능
    public Page<ArticleDto> searchArticles(SearchType searchType,
                                           String searchKeyword, Pageable pageable) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId){
        return null;
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(long l) {
        return null;
    }

    public void saveArticle(ArticleDto dto) {

    }

    public void updateArticle(ArticleDto dto) {

    }

    public void deleteArticle(long articleId) {
    }
}
