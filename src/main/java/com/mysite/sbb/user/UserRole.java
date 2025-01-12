package com.mysite.sbb.user;

import lombok.Getter;

/**
 * Spring Security에서 사용할 권한 코드 관리 파일
 */
@Getter
public enum UserRole {
    USER("ROLE_USER"),  //일반 사용자 권한
    ADMIN("ROLE_ADMIN");

    private String value;

    UserRole(String value){
        this.value = value;
    }
}
