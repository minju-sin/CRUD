package com.ancho.crud.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/* 댓글 클래스 */

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createBy"),
})
@Entity
public class ArticleComment extends AuditingFields{
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

//     연관 관계를 선언하기 위해 @ManyToOne 사용
     @Setter private @ManyToOne(optional = false) Article article;  //  게시글 (ID)
     @Setter private @ManyToOne(optional = false) UserAccount userAccount; //   유저 정보(ID)
     @Setter @Column(nullable = false, length = 500) private String content; //  본문

     protected ArticleComment() {}

     private ArticleComment(Article article,UserAccount userAccount, String content) {
          this.article = article;
          this.userAccount = userAccount;
          this.content = content;
     }

     public static ArticleComment of(Article article,UserAccount userAccount, String content) {
          return new ArticleComment(article, userAccount, content);
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (!(o instanceof ArticleComment that)) return false;
          return id != null && id.equals(that.id);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id);
     }
}
