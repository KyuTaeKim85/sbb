package com.mysite.sbb.answer;


import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {
    @Id     //Primary Key로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;     //질문 데이터의 고유 번호

    @Column(columnDefinition = "TEXT")  //문자열 길이 제한 제거
    private String content;

    private LocalDateTime createDate;   //답변 데이터 생성일자
    private LocalDateTime modifyDate;   //답변 데이터 수정일자

    @ManyToOne  //Answer(답변)여러개이고, Question(질문)은 1개 -> N:1
    private Question question;  //Question 클래스

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    @OneToMany(mappedBy = "answer")
    private List<Comment> commentList;

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                '}';
    }
}
