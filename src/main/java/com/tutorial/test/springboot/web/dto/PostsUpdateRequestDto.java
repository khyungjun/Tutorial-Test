package com.tutorial.test.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 이거 없어서 수정하기 Test에서 헤맸음.
@Getter
public class PostsUpdateRequestDto { // PostsUpdateRequestDto와 Posts의 udpate() 메소드가 책에 없음.(git 코드에 있음)
	
    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
}
