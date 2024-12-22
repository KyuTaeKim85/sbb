package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HelloLombok2 {
	private final String hello;			  // 멤버 변수(member variable)
	private final int lombok;

	// @RequiredArgsConstructor 어노테이션으로 대체함
//	public HelloLombok2(String hello, int lombok) {   // 생성자(Constructor)
//		this.hello = hello;
//		this.lombok = lombok;
//	}
	// @Getter 어노테이션으로 대체함
//	public String getHello() {
//		return hello;
//	}
	// @Getter 어노테이션으로 대체함
//	public String getLombok() {
//		return lombok;
//	}
	public static void main(String[] args) {   // 메소드
		HelloLombok2 helloLombok2 = new HelloLombok2("헬로2", 5);
		System.out.println("롬북");
		System.out.println(helloLombok2.getHello());
		System.out.println(helloLombok2.getLombok());
	}

}
