package ancho.crud.domain;

import java.time.LocalDateTime;

/* 댓글 클래스 */
public class ArticleComment {
     private Long id;
     private Long article;  //  게시글 (ID)
     private String content; //  본문


     private LocalDateTime createdAt;    // 생성일시
     private String createBy;    // 생성자
     private LocalDateTime modifiedAt;   // 수정일시
     private String modifiedBy;  //  수정자
}
