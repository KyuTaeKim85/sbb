package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller  // 해당 클래스는 Web주소를 받아 화면까지 처리할 수 있도록 해주는 Annoation
public class HelloController {
	@GetMapping("/hello") // 브라우저에서 입력한 주소에 매칭이 되어 실행해주는 Annotation
	@ResponseBody 		  // return값을 브라우저에 그대로 출력하기 위한 Annoatation
	public String hello() {
		return "Hello Spring Boot Board";
	}
	
	@GetMapping("/jump")
	@ResponseBody
	public String test1() {
		return "점프 투 스프링 부트";
	}
	
	@GetMapping("/jump2")
	@ResponseBody
	public String test2() {
		return "점프 투 스프링 부트2";
	}

	@GetMapping("/jump3")
	@ResponseBody
	public String test3() {
		return "점프 투 스프링 부트3";
	}

	// 404 http status code: 페이지가 없는 에러
	// 500 http status code: 서버에서 예외가 발생한 에러
	@GetMapping("/sbb")	
	public String index() {
		return "index";
	}
}


