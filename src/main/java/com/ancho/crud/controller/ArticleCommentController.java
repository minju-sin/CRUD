package com.ancho.crud.controller;

import com.ancho.crud.dto.request.ArticleCommentRequest;
import com.ancho.crud.dto.security.BoardPrincipal;
import com.ancho.crud.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping ("/new")
    public String postNewArticleComment(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            ArticleCommentRequest articleCommentRequest
    ) {
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));
//        // TODO: 인증 정보를 넣어줘야 한다.
//        // 현재는 인증 정보를 구현하지 않았으므로 가짜 데이터를 넣어준다.
//        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
//                "ancho", "intel1125@#", "ancho@mail.com", null, null
//        )));

        //  댓글을 추가하면 댓글을 추가할 게시글에 바로 게시글이 나타나야 한다.
        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping ("/{commentId}/delete")
    public String deleteArticleComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            Long articleId
    ) {
        articleCommentService.deleteArticleComment(commentId, boardPrincipal.getUsername());

        //  댓글을 삭제하면, 해당하는 게시글에서 삭제가 되어야하므로 게시글 아이디를 리턴한다.
        return "redirect:/articles/" + articleId;
    }

}