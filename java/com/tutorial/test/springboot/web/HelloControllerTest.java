package com.tutorial.test.springboot.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
// @RunWith(SpringRunner.class) => JUnit4에서 사용
// JUnit5로 넘어오면서 @RunWith는 @ExtendWith로 변환하게 되었습니다. @RunWith(SpringRunner.class) => @ExtendWith(SpringExtension.class)
// 테스트를 진행 할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킨다.
// 여기서는 SpringRunner라는 스프링 실행자를 사용한다.
// 즉, 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 한다.

// @SpringBootTest
// 정리하자면, 최근 스프링 부트는 JUnit5를 사용하기 때문에 더이상 JUnit4에서 제공하던 @RunWith를 쓸 필요가 없고 (쓰고 싶으면 쓸 수는 있지만), 
// @ExtendWith를 사용해야 하지만, 이미 스프링 부트가 제공하는 모든 테스트용 애노테이션(@SpringBootTest) 에 
// 메타 애노테이션으로 적용되어 있기 때문에 @ExtendWith(SpringExtension.class)를 생략할 수 있다.

// 1. @SpringBootTest가 아닌 @RunWith(SpringRunner.class)를 사용하는 이유??
// => @SpringBootTest를 사용하면 application context를 전부 로딩해서 자칫 잘못하면 무거운 프로젝트로서의 역할을 할 수 있습니다. 
//    하지만 Junit4에서 지원하는 @RunWith(SpringRunner.class)를 사용한다면, @Autowired, @MockBean에 해당되는 것들에만 application context를 로딩하게되므로 Junit4에서는 필요한 조건에 맞춰서 @RunWith(SpringRunner.class)를 사용합니다.
// 2. @RunWith와 더불어 @RunWith(SpringRunner.class) -> 정확히 무슨 역할을 하는지??
// => SpringRunner에 대한 별칭으로 SpringJUnit4ClassRunner, JUnit 테스트 라이브러리를 Spring TestContext Framework와 결합합니다. 결합한 이것을 @RunWith(SpringRunner.class)라고 일컫습니다.

@WebMvcTest
// 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션이다.
// 선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있다.
// 단. @Service, @Component, @Repository 등은 사용할 수 없다.
// 여기서는 컨트롤러만 사용하기 때문에 선언한다.
public class HelloControllerTest {

	@Autowired // 스프링이 관리하는 빈(Bean)을 주입 받는다.
	private MockMvc mvc; // 웹 API를 테스트할 때 사용한다. 스프링 MVC 테스트의 시작점이다. 이 클래스를 통해 HTTP GET, POST 등에 대한 API를 테스트 할 수 있다.
	
	@Test
	public void hello가_리턴된다() throws Exception {
		String hello = "hello";
		
		mvc.perform(get("/hello")) // MockMvc를 통해 /hello 주소로 HTTP GET 요청을 한다. 체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언할 수 있다.
				.andExpect(status().isOk()) // mvc.perform의 결과를 검증한다. HTTP Headr의 Status를 검증한다. 우리가 흔히 알고 있는 200, 404, 500 등의 상태를 검증한다. 여기선 OK 즉, 200인지 아닌지를 검증한다.
				.andExpect(content().string(hello)); // mvc.perform의 겨로가를 검증한다. 응답 본문의 내용을 검증한다. Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증한다.	
	}
	
	@Test
	public void helloDto가_리턴된다() throws Exception {
		String name = "hello";
		int amount = 1000;
		
		mvc.perform(get("/hello/dto")
				.param("name", name) // API 테스트할 때 사용될 요청 파라미터를 설정한다. 단, 값은 String만 허용한다. 그래서 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능하다.
				.param("amount", String.valueOf(amount)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(name))) // JSON 응답값을 필드별로 검증할 수 있는 메소드이다. $를 기준으로 필드명을 명시한다. 여기서는 name과 amount를 검증하니 $.name, $.amount로 검증한다.
				.andExpect(jsonPath("$.amount", is(amount)));
	}
	
}
