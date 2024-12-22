package com.mysite.sbb.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends JpaRepository <Question, Integer>{
    //제목으로 조회하는 쿼리
    Question findBySubject(String subject);

    //제목과 내용의 조건 값이 같은 경우에만 조회하는 쿼리
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    List<Question> findBySubjectOrContent(String subject, String content);
    List<Question> findBySubjectIn(List<String> subjects);
    List<Question> findByCreateDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
