package com.mysite.sbb.question;


import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private LocalDateTime modifyDate;   //질문 데이터 수정일자

    //@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;
    
    /*
    List(Array), Set
    List(배열)
    1. 값을 넣을 때 중복을 허용
    2. 순서가 있음.
    Set(집합)
    1. 중복을 허용하지 않음
    2. 순서가 없음
     */

    @ManyToMany
    Set<SiteUser> voter;

    @OneToMany(mappedBy = "question")
    private List<Comment> commentList;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", answerList=" + answerList +
                '}';
    }
}
