package com.tutorial.test.springboot.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// 여태 src/test/resources의 application.yml이 적용되지 않고 src/main.resources의 application.yml이 적용되고 있었다.
// intelliJ에서는 테스트 시에  src/test/resources의 application.yml이 있다면 자동으로 해당 application.yml을 적용시킨다고 하는데 sts에서는 안되는 것 같다.
// 여러 방법을 시도해봤지만 되지 않아 결국 application-test.yml로 파일명을 바꾸고 아래에 해당 설정을 적용시켰다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.yml"})
public class ProfileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void profile은_인증없이_호출된다() throws Exception {
        String expected = "default";

        ResponseEntity<String> response = restTemplate.getForEntity("/profile", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }
    
}
