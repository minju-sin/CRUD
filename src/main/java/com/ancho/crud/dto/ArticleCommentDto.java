package com.ancho.crud.dto;

import com.ancho.crud.domain.Article;
import com.ancho.crud.domain.ArticleComment;
import com.ancho.crud.domain.UserAccount;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.ancho.crud.domain.ArticleComment}
 */
public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        String createBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleCommentDto of(Long articleId, UserAccountDto userAccountDto, String content) {
        return new ArticleCommentDto(null, articleId, userAccountDto, content, null, null, null, null);
    }

    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, String content, LocalDateTime createdAt, String createBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleCommentDto(id, articleId, userAccountDto, content, createdAt, createBy, modifiedAt, modifiedBy);
    }

    public static ArticleCommentDto from(ArticleComment entity){
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreateBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount){
        return ArticleComment.of(
                article,
                userAccount,
                content
        );
    }
}