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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/* 게시글 클래스 */

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createBy"),
})
@EntityListeners(AuditingEntityListener.class)
//  현재 JpaConfig에서는 Auditing 세팅이 되어있지만 추가적으로 하나 더작성해야하는데,
//  바로 엔티티에서도 Auditing을 쓰겠다는 표시를 해줘야한다. domain/Article에서 아래의 내용을 작성해야한다.
//@EntityListeners(AuditingEntityListener.class)
//@EntityListeners - Entity를 DB에 적용하기 이전, 이후에 커스텀 콜백을 요청할 수 있는 어노테이션
//AuditingEntityListener - Entity 영속성 및 업데이트에 대한 Auditing 정보를 캡처하는 JPA Entity Listener
//이 문구는 당연히 ArticleComment 엔티티에도 작성되어있어야 한다. 그래야 Auditing을 쓸수 있기 때문이다.
@Entity
public class Article {
    /* primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //  자동으로 부여


    @Setter @Column(nullable = false) private String title;   //  제목
    @Setter @Column(nullable = false, length = 10000) private String content; //  본문

    @Setter private String hashtag; //  해시태그

//    댓글의 경우 게시글은 하나지만 댓글은 여러개 일수 있으므로 @ManyToOne 을 추가했다.
//    양방향 바인딩
    @ToString.Exclude   //  퍼포먼스나 메모리 저하를 방지하기 위해 선언
    @OrderBy("id")  //  id 기준 정렬
    @OneToMany(mappedBy ="article", cascade = CascadeType.ALL )
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    /* 업데이트를 할 때마다 자동 셋팅하는 방법
    * config 파일 - JpaConfig에
    * @EnableJpaAuditing 와 @Configuration 선언
    */
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;    // 생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createBy;    // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt;   // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy;  //  수정자



    /*
    * 모든 JPA 엔티티들은 hibernate 구현체를 사용하는 기준으로 설명하면, 기본 생성자를 가지고 있어야 한다.
    * 평소에는 오픈하면 안되므로 protected 사용
    */
    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }


    /*
    * 동등성 검사
    * @equlas @hashcode
    * 모든 것을 동등성 검사할 필요가 없다
    * 현재는 id만 동등한지 검사하면 됨
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
        /*
        * "새로 만들어진 엔티티, 즉 영속화 되지 않은 엔티티는 동등성 검사에서 탈락시킬거야!"라는 뜻이다.
        * 글의 내용과 날짜와 같은 필드의 데이터들이 모두 동일할 지언정 id값이 없거나 동일하지 않다?
        * 그러면 탈락시키게 된다.
        */
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
