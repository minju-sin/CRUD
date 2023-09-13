package com.ancho.crud.service;

import com.ancho.crud.domain.ArticleComment;
import com.ancho.crud.dto.ArticleCommentDto;
import com.ancho.crud.repository.ArticleCommentRepository;
import com.ancho.crud.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComment(Long articleId) {
        return List.of();
    }

    public void saveArticleComment(ArticleCommentDto dto){}

    public void updateArticleComment(ArticleCommentDto dto){}

    public void deleteArticleComment(Long articleCommentId){}
}