package com.ancho.crud.dto;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.ancho.crud.domain.Article}
 */
public record ArticleUpdateDto(
        String title,
        String content,
        String hashtag
) {
    public static ArticleUpdateDto of(String title, String content, String hashtag) {
        return new ArticleUpdateDto(title, content, hashtag);
    }
}
