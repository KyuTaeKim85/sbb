package com.mysite.sbb.question;

import com.mysite.sbb.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    //Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
    Page<Question> findByAuthor(SiteUser siteUser, Pageable pageable);

    //JPQL쿼리
    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
