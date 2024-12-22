package com.mysite.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // spring boot를 실행시키기 위한 Annotation
public class SbbApplication {

	public static void main(String[] args) {
		// spring boot()
		SpringApplication.run(SbbApplication.class, args);
	}

}
