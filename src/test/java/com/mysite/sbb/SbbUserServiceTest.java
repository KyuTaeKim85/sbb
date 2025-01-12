package com.mysite.sbb;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SbbUserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원가입및탈퇴테스트(){
        String username ="test1";
        String email ="test1@gmail.com";
        String password ="1234";

        //회원가입 시도
        SiteUser user = userService.create(username, email, password);

        assertEquals(username, user.getUsername());
        //assertEquals(password, user.getPassword());     //error. 암호 평문과 암호화된 값은 다름
        assertEquals(email, user.getEmail());

        //회원가입 시도 성공 시에 바로 탈퇴처리
        //userRepository.delete(user);

    }
}
