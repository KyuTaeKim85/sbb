package com.mysite.sbb.user;

import com.mysite.sbb.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service    //해당 클랴스 객체는 모두 spring에서 관리할 수 있도록 서비스로 등록
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 관련
     * @param username
     * @param email
     * @param password
     * @return
     */
    public SiteUser create(String username, String email, String password){
        SiteUser siteUser = new SiteUser();

        siteUser.setUsername(username);
        siteUser.setEmail(email);

        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        siteUser.setPassword(passwordEncoder.encode(password)); //평문 password 암호화 하여 저장

        return this.userRepository.save(siteUser);
    }

    /**
     * username으로 사용자 정보 가져오기
     * @param username
     * @return
     */
    public SiteUser getUser(String username){
        Optional<SiteUser> optSiteUser = this.userRepository.findByusername(username);
        if(optSiteUser.isPresent()){
            return optSiteUser.get();
        }else{
            throw new DataNotFoundException("siteuser not found");
        }
    }

    /**
     * 패스워드 업데이트
     * @param siteUser
     * @param password
     * @return
     */
    public SiteUser update(SiteUser siteUser, String password){

        siteUser.setPassword(password);

        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        siteUser.setPassword(passwordEncoder.encode(password)); //평문 password 암호화 하여 저장

        return this.userRepository.save(siteUser);
    }

}
