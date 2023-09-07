package com.ancho.crud.domain;

//    반복 코드 추출
//    Article 도메인에서 생성, 수정자/날짜 내용이 같은 부분이 많아 추출 가능

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
//  현재 JpaConfig에서는 Auditing 세팅이 되어있지만 추가적으로 하나 더작성해야하는데,
//  바로 엔티티에서도 Auditing을 쓰겠다는 표시를 해줘야한다. domain/Article에서 아래의 내용을 작성해야한다.
//@EntityListeners(AuditingEntityListener.class)
//@EntityListeners - Entity를 DB에 적용하기 이전, 이후에 커스텀 콜백을 요청할 수 있는 어노테이션
//AuditingEntityListener - Entity 영속성 및 업데이트에 대한 Auditing 정보를 캡처하는 JPA Entity Listener
//이 문구는 당연히 ArticleComment 엔티티에도 작성되어있어야 한다. 그래야 Auditing을 쓸수 있기 때문이다.
@MappedSuperclass
public abstract class AuditingFields {
    //  해당 필드들이 실제 웹 화면에서 보여줄 때,
    //  웹 화면에서 파라미터를 받아 세팅할 땐 파싱이 잘 되어있어야 함
    //  파싱에 관련된 룰인 @DateTimeFormat 추가해주는 것이 좋음
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)    // updatable : 만들어지는 순간 기록되는 데이터로 변경 불가 표시함
    private LocalDateTime createdAt;    // 생성일시

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String createBy;    // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;   // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;  //  수정자

}
