package com.tutorial.test.springboot.domain.posts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// MyBatis에서는 Dao라고 불리는 DB Layer 접근자로 JPA에선 Repository라고 부르며 인터페이스를 생성 후 JpaRepository<Entity 클래스, PK 타입>를 상속하면 기본적이 CRUD 메소드가 자동으로 생성된다.
// @Respository를 추가할 필요는 없고 Entity 클래스와 기본 Entity Repository는 함께 위치해야 한다. 도메인 패키지에서 함께 관리하도록 한다.
public interface PostsRepository extends JpaRepository<Posts, Long> { 

	// SpringDataJpa에서 제공하지 않는 메소드는 이렇게 쿼리로 작성해도 된다. 
	// 실제로 이 코드는 SpringDataJpa에서 제공하는 기본 메소드만으로도 해결할 수 있으나 @Query가 훨씬 가독성이 좋으니 선택해서 사용하도록 한다.
	@Query("SELECT p FROM Posts p ORDER BY p.id DESC") 
	List<Posts> findAllDesc();
	
}
