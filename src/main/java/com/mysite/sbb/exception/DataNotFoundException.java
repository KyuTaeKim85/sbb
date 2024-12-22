package com.mysite.sbb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//프론트에 예외 응답값을 주기 위해 사용하는 어노테이션
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "요청하신 엔티티 객체가 없음")
public class DataNotFoundException extends RuntimeException{
    private  static final  long serialVersionUID = 1L;
    public  DataNotFoundException(String message){
        super(message);
    }
}
