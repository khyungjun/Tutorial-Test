package com.tutorial.test.springboot.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.test.springboot.domain.posts.Posts;
import com.tutorial.test.springboot.domain.posts.PostsRepository;
import com.tutorial.test.springboot.web.dto.PostsSaveRequestDto;
import com.tutorial.test.springboot.web.dto.PostsUpdateRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 랜덤 포트 실행
public class PostsApiControllerTest {

	@LocalServerPort
	private int port;

	// @WebMvcTest를 사용하지 않은 이유는 @WebMvcTest의 경우 JPA 기능이 작동하지 않기 때문이다.
	// Controller와 ControllerAdvice 등 외부 연동과 관련된 부분만 활성화된다.
	// JPA 기능까지 한 번에 테스트 할 때는 @SpringBootTest와 TestRestTemplate를 사용하도록 한다.
//	@Autowired
//	private TestRestTemplate restTemplate;
	
	@Autowired
	private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

//  @Before // 매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성한다.
//  @Before(value = "execution(* com.test.controller.TestController.*(..))") // Test 실행해보니 이 방법은 안된다. setup()이 되지 않아 NullPointerException 발생!
    @BeforeEach // JUnit 5.x에서는 @BeforeEach를 사용한다. @After, @AfterEach는 실행후 처리에 대해 지정할 수 있다. 참고 : https://velog.io/@urtimeislimited/TIL-MockMVC%EC%99%80-NullPointException
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    
//	@After(value = "execution(* com.test.controller.TestController.*(..))") // 참고 : https://velog.io/@hamzzi/Spring-Boot-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EC%A4%91-After-%EC%98%A4%EB%A5%98-%ED%95%B4%EA%B2%B0
	@AfterEach
	public void tearDown() throws Exception {
		postsRepository.deleteAll();
	}
	
	@Test
	@WithMockUser(roles="USER") // 인증된 모의(가짜) 사용자를 만들어서 사용한다. roles에 권한을 추가할 수 있다. 즉, 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 된다.
	public void Posts_등록된다() throws Exception {
		// given
		String title = "title";
		String content = "content";
		PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
				.title(title)
				.content(content)
				.author("author")
				.build();
		
		String url = "http://localhost:" + port + "/api/v1/posts";
		
		// when
//		ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url) // mvc.perform : 생성된 MockMvc를 통해 API를 테스트 한다. 본문(Body) 영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환한다.
//                .contentType(MediaType.APPLICATION_JSON_UTF8) // 참고 : https://smpark1020.tistory.com/166
        		.contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());
		
		
		// then
//		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(responseEntity.getBody()).isGreaterThan(0L);
		
		List<Posts> all = postsRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(title);
		assertThat(all.get(0).getContent()).isEqualTo(content);
	}
	
	@Test
	@WithMockUser(roles="USER")
	public void Posts_수정된다() throws Exception {
		// given
		Posts savedPosts = postsRepository.save(Posts.builder()
				.title("title")
				.content("content")
				.author("author")
				.build());
				
		Long updateId = savedPosts.getId();
		String expectedTitle = "title2";
		String expectedContent = "content2";
		
		PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
				.title(expectedTitle)
				.content(expectedContent)
				.build();

		String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
		
//		HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
		
		// when
//		ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(put(url)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());
		
		// then
//		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(responseEntity.getBody()).isGreaterThan(0L);
		
		List<Posts> all = postsRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
	}
	
}
