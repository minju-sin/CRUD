package com.ancho.crud.repository;

import com.ancho.crud.domain.Article;
import com.ancho.crud.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//API 검색 기능 구현
@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 모든 entity 안에 있는 모든 필드에 있는 기본 검색 기능 구현해 줌
        //  추가로 검색 기능 구현
        QuerydslBinderCustomizer<QArticle>  //  BinderCustomizer 클래스에서는 반드시 queue 클래스를 넣어줘야 함
{
    //  Containing은 부분 검색도 가능하게 만들어 준다.
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);
    @Override   //  customize 오버라이드 필요
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true);    // 선택한 내용만 볼 수 있도록 true로 설정해 줌 (기본값은 false)
//          excluding() 에서 괄호 안에 검색해서 보고 싶은 내용을 넣어줌 (여기서는 제목, 내용, 해시태그, 생성일시, 생성자 검색 가능)
//        공부 목적으로 이용하는 게시판이라 content를 삽입했지만, 너무 커서.. 추천하지는 않음
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createBy);
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);  //  쿼리 생성문 : like '${value}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);  //  쿼리 생성문 : like '%${value}%' - 편리하게 사용
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);  //    날짜 검사 - 더 편리한 날짜 검사는 추후에 할 예정
        bindings.bind(root.createBy).first(StringExpression::containsIgnoreCase);


    }
}
