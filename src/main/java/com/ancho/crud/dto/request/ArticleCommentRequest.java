package com.ancho.crud.dto.request;

import com.ancho.crud.dto.ArticleCommentDto;
import com.ancho.crud.dto.UserAccountDto;

/**
 * DTO for {@link com.ancho.crud.domain.ArticleComment}
 */
public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }
}