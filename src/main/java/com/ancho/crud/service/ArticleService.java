package com.ancho.crud.service;

import com.ancho.crud.domain.Article;
import com.ancho.crud.domain.type.SearchType;
import com.ancho.crud.dto.ArticleDto;
import com.ancho.crud.dto.ArticleWithCommentsDto;
import com.ancho.crud.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * 게시판 서비스 구현
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true) // 읽어주는 작업만 할 때 붙여주는 기능
    public Page<ArticleDto> searchArticles(SearchType searchType,
                                           String searchKeyword, Pageable pageable) {
        //  검색어 없는 경우 -> 아무 일도 일어나지 않고, 그냥 페이지 보여줌
        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        //  검색어 존재하는 경우
        //  swith 구문 자체를 return 할 수 있다.
        //  searchKeyword로 해당 검색어를 찾아 return 한다.
        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId){
        //  게시글 조회
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(()->new EntityNotFoundException("게시글이 없습니다 - articleId:" + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        //  게시글 정보 입력하면, 게시글 생성한다.
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            //  게시글 수정
            Article article = articleRepository.getReferenceById(dto.id());
            if (dto.title() != null) { article.setTitle(dto.title()); }
            if (dto.content() != null) { article.setContent(dto.content()); }
            article.setHashtag(dto.hashtag());
        }catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }

    }

    public void deleteArticle(long articleId) {
        //  삭제
        articleRepository.deleteById(articleId);
    }
}
