package com.mysite.sbb.question;


import com.mysite.sbb.answer.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Question {
    @Id     //Primary Key로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;     //질문 데이터의 고유 번호

    @Column(length = 200)   //문자열 길이를 200으로
    private String subject;     //질문 데이터의 제목

    @Column(columnDefinition = "TEXT")
    private String content;     //질문 데이터의 내용

    private LocalDateTime createDate;   //질문 데이터 생성일자

    //@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}
