package com.tutorial.test.springboot.domain;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들(createDate, modifiedDate)도 칼럼으로 인식하도록 한다.
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity 클래스에 Auditing 기능을 포함시킨다.
public class BaseTimeEntity { // 모든 Entity의 상위 클래스가 되어 Entity들의 createdDate, modifiedDate를 자동으로 관리하는 역할을 한다.

	@CreatedDate // Entity가 생성되어 저장될 때 시간이 자동 저장된다.
	private LocalDateTime createdDate;
	
	@LastModifiedDate // 조회한 Entity의 갑승ㄹ 변경할 때 시간이 자동 저장된다.
	private LocalDateTime modifiedDate;
	
}
