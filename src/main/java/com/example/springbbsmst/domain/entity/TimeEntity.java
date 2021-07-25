package com.example.springbbsmst.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter // lombok
@MappedSuperclass  // 테이블로 매핑하지 않고, 자식 클래스(엔티티)에게 매핑정보를 상속하기 위한 어노테이션
@EntityListeners(AuditingEntityListener.class) //JPA 에게 해당 엔티티는 auditing 기능을 사용한다는 것을 알려줌.
public abstract class TimeEntity {
    @CreatedDate // 이게 없으면 수정될 때 값이 null이 됨. 생성일을 주입함.
    @Column(updatable = false) // 업데이트할 필요가 없어서 false
    private LocalDateTime createdDate;

    @LastModifiedDate // 엔티티가 수정되면 마지막 수정일시를 넣어줌.
    private LocalDateTime modifiedDate;
}
