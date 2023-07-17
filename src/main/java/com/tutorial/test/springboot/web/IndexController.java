package com.tutorial.test.springboot.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tutorial.test.springboot.config.auth.LoginUser;
import com.tutorial.test.springboot.config.auth.dto.SessionUser;
import com.tutorial.test.springboot.service.PostsService;
import com.tutorial.test.springboot.web.dto.PostsResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {

	private final PostsService postsService;
	private final HttpSession httpSession;
	
//	public String index() {
//		return "index";
//	}
//	public String index(Model model) {
//		model.addAttribute("posts", postsService.findAllDesc());
//		return "index";
//	}
//	@GetMapping("/")
//	public String index(Model model) {
//		model.addAttribute("posts", postsService.findAllDesc());
//
////		SessionUser user = (User) httpSession.getAttribute("user"); 책 오류
//		SessionUser user = (SessionUser) httpSession.getAttribute("user"); // 앞서 작성된 CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성했다. 즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있다.
//		
//		if(user != null) { // 세션에 저장된 값이 있을 때만 model에 userName으로 등록한다. 세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보이게 된다.
//			model.addAttribute("userName", user.getName());
//		}
//		
//		return "index";
//	}
	@GetMapping("/")
	public String index(Model model, @LoginUser SessionUser user) { // @LoginUser SessionUser user : 기존에 (User) httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선되었다. 이제는 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 되었다.
		model.addAttribute("posts", postsService.findAllDesc());

		if(user != null) { // 세션에 저장된 값이 있을 때만 model에 userName으로 등록한다. 세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보이게 된다.
			model.addAttribute("userName", user.getName());
		}
		
		return "index";
	}
		
	@GetMapping("/posts/save")
	public String postsSave() {
		return "posts-save";
	}

	@GetMapping("/posts/update/{id}")
	public String postsUpdate(@PathVariable Long id, Model model) {
		PostsResponseDto dto = postsService.findById(id);
		model.addAttribute("post", dto);
		
		return "posts-update";
	}
}
