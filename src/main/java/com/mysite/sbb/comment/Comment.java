package com.mysite.sbb.comment;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {
    @Id     //Primary Key로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;     //댓글 데이터의 고유 번호

    @Column(columnDefinition = "TEXT")
    private String content;     //댓글 내용

    private LocalDateTime createDate;   //댓글 데이터 생성일자
    private LocalDateTime modifyDate;   //댓글 데이터 수정일자

    @ManyToOne
    private SiteUser author;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Answer answer;

    public Integer getQuestionId() {
        Integer result = null;
        if (this.question != null) {
            result = this.question.getId();
        } else if (this.answer != null) {
            result = this.answer.getQuestion().getId();
        }
        return result;
    }
}
