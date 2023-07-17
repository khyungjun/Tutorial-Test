package com.tutorial.test.springboot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tutorial.test.springboot.domain.posts.Posts;
import com.tutorial.test.springboot.domain.posts.PostsRepository;
import com.tutorial.test.springboot.web.dto.PostsListResponseDto;
import com.tutorial.test.springboot.web.dto.PostsResponseDto;
import com.tutorial.test.springboot.web.dto.PostsSaveRequestDto;
import com.tutorial.test.springboot.web.dto.PostsUpdateRequestDto;

import lombok.RequiredArgsConstructor;

// Controller와 Service에 Autowired가 없는 것이 어색할 수 있으나
// 스프링에서 Bean을 주입 받는 방식은 @Autowired, setter, 생성자 가 있다.
// 이 중에서 가장 권장하는 방식은 생성자로 주입받는 방식이다.(@Autowired는 권장하지 않는다.)
// 즉, 생성자로 Bean 객체를 받도록 하면 @Autowired와 동일한 효과를 볼 수 있다.
// 롬복의 @RequiredArgsConstructor를 사용하여 final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성한다.
// 이렇게 하면 해당 클래스의 의존성 관계가 변경될 때마다 생성자 코드를 계속해서 수정하는 번거로움을 해결할 수 있다. 
// 즉, 해당 컨트롤러에 새로운 서비스를 추가하거나, 기존 컴포넌트를 제거하는 드으이 상황이 발생해도 생성자 코드는 전혀 손대지 않아도 된다.
@RequiredArgsConstructor
@Service
public class PostsService {

	private final PostsRepository postsRepository;
	
	@Transactional
	public Long save(PostsSaveRequestDto requestDto) {
		return postsRepository.save(requestDto.toEntity()).getId();
	}
	
	@Transactional
	public Long update(Long id, PostsUpdateRequestDto requestDto) {
		Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
		
		posts.update(requestDto.getTitle(), requestDto.getContent());
		
		return id;
	}
	
	public PostsResponseDto findById(Long id) {
		Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
		
		return new PostsResponseDto(entity);
	}

	@Transactional(readOnly = true) // 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선되기 때문에 등록,수정,삭제 기능이 전혀 없는 서비스 메소드에서 사용하도록 한다.
	public List<PostsListResponseDto> findAllDesc() {
		return postsRepository.findAllDesc().stream()
				.map(PostsListResponseDto::new) // .map(posts -> new PostsListResponseDto(posts))와 같다.
				.collect(Collectors.toList());
	}

	@Transactional
	public void delete (Long id) {
		Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
		postsRepository.delete(posts); 	// JpaRepository에서 이미 delete 메소드를 지원하고 있으니 이를 활용한다.
										// 엔티티를 파라미터로 삭제할 수도 있고, deleteById 메소드를 이용하면 id로 삭제할 수도 있다.
										// 존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제한다.
	}
	
}
