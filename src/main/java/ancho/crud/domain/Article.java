package ancho.crud.domain;

import java.time.LocalDateTime;

/* 게시글 클래스 */
public class Article {
    private Long id;
    private String title;   //  제목
    private String content; //  본문
    private String hashtag; //  해시태그


    private LocalDateTime createdAt;    // 생성일시
    private String createBy;    // 생성자
    private LocalDateTime modifiedAt;   // 수정일시
    private String modifiedBy;  //  수정자

}
