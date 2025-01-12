package com.mysite.sbb.user;

import com.mysite.sbb.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SiteUser {
    @Id  // Primary Key로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 고유번호 자동 생성
    private Long id;

    @Column(unique = true)
    private String username;        // login id
    private String password;        // login password

    @Column(unique = true)
    private String email;           // 사용자 이메일

    /*@OneToMany(mappedBy = "author")
    private Question question;*/
}
