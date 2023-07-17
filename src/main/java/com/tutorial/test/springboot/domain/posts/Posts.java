package com.tutorial.test.springboot.domain.posts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.tutorial.test.springboot.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // 클래스 내 모든 필드의 Getter 메소드를 자동생성
@NoArgsConstructor // 기본 생성자 자동 추가. public Posts() {}와 같은 효과
@Entity // 테이블과 링크될 클래스임을 나타낸다. 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭한다. ex) SalesManager.java -> sales_manager table
// Entity 클래스에서는 절대 Setter 메소드를 만들지 않도록 한다. 해당 필드의 값 변경이 피룡하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가하도록 한다.
public class Posts extends BaseTimeEntity { // 주요 어노테이션을 클래스에 가깝게 두기

	@Id // 해당 테이블의 PK 필드를 나타낸다.
	@GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue : PK의 생성 규칠을 나타낸다. 스프링 부트 2.0에서는 GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
	private Long id;
	
	@Column(length = 500, nullable = false) 
	// 테이블의 컬럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모드 칼럼이 된다. 사용하는 이뉴는, 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용한다.
	// 문자열의 경우 VARCHAR(255)가 기본값인데, 사이즈를 500으로 늘리고 싶거나(ex. title) 타입을 TEXT(ex. content)로 변경하고 싶거나 등의 경우에 사용된다.
	private String title;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	private String author;
	
	@Builder // 해당 클래스의 빌더 패턴 클래스를 생성. 생성자 상단에 선언 시 생성자에 포함된 빌드만 빌더에 포함.
	public Posts(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}
	
	// 객체지향 프로그래밍에서 클래스의 상태를 변경하는 것은 자기 자신이 해야할 일이라고 나와있습니다. 그렇기 때문에 update메서드가 Posts 도메인 안에 있는 것입니다.
	// 객체지향 프로그래밍에서 객체의 상태 변경은 그 객체가 해야할 일이기 때문이며, 기존에 JPA가 없던 시절에는 객체가 본인의 값을 변경하는데도, 매번 DB에 update 쿼리를 일으켜야되서 이 원칙을 지키기 힘들었습니다.
	// JPA가 이를 지원해준뒤로 DB를 사용하는 애플리케이션에서도 객체지향이 가능하게 된것입니다 :)
	public void update(String title, String content) {
	    this.title = title;
	    this.content = content;
	}
}
