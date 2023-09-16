package com.ancho.crud.controller;

import com.ancho.crud.domain.type.SearchType;
import com.ancho.crud.dto.response.ArticleResponse;
import com.ancho.crud.dto.response.ArticleWithCommentsResponse;
import com.ancho.crud.service.ArticleService;
import com.ancho.crud.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/*
 * /articles
 * /articles/{article-id}
 * /articles/search
 * /articles/search-hashtag
 */
@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    //    게시판 페이지
    @GetMapping
    public String articles(
            @RequestParam(required = false)SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ){
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        //  페이지네이션 barNumbers 는 페이지네이션 서비스 안에 존재하는 getPaginationBarNumbers(현재 페이지, 전체 페이지) 함수를 이용해 구할 수 있다.
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        return "articles/index";
    }

    //  게시글 페이지
    @GetMapping("/{articleId}") //  게시글 아이디를 받음
    public String article(@PathVariable Long articleId, ModelMap map) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        map.addAttribute("totalCount",articleService.getArticleCount());

        return "articles/detail";
    }
}
