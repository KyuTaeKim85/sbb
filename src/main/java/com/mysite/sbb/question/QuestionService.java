package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service    //해당 클랴스 객체는 모두 spring에서 관리할 수 있도록 서비스로 등록
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    /**
     * 모든 게시물 목록 가져오기
     * @return
     */
    public List<Question> getList(){
        List<Question> questionList = questionRepository.findAll();
        
        // 고객의 요구사항에 맞는 복잡한 기능의 코드를 작성
        // 예시1: 방문자 정보(성별, 생년월일 등)를 데이ㅓ를 수집하느 코드
        // 예시2: 다른 회사의 API를 호출하는 기능(방문자의 접속 정보(현재 위치)로 지도로 표시하기.)
        
        return questionList;
    }

    /**
     * 페이징처리
     * @param page
     * @return
     */
    public Page<Question> getList(int page, String keyword){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));   //page값에 맞는 게시판 글을 10개를 표출
        //return this.questionRepository.findAll(pageable);

        return questionRepository.findAllByKeyword(keyword, pageable);

        //Specification<Question> spec = search(keyword);
        //return this.questionRepository.findAll(spec, pageable); //검색기능을 더한 전체조회

    }

    public Question getQuestion(Integer id){
        Optional<Question> optQuestion =  this.questionRepository.findById(id);
        if(optQuestion.isPresent()){
            return optQuestion.get();
        }else{
            throw new DataNotFoundException("question not found"); //사용자 정의 예외 객체 호출
        }
    }

    /**
     * 질문 생성하기
     * @param
     * @param content
     */
    public void create(String subject, String content, SiteUser siteUser){
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(siteUser);
        this.questionRepository.save(q);
    }

    public Page<Question> getListByAuthor(int page, SiteUser siteUser) {
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return this.questionRepository.findByAuthor(siteUser, pageable);
    }


    /**
     * 질문 수정하기
     * @param
     * @param content
     */
    public void modify(Question q, String subject, String content){
        q.setSubject(subject);
        q.setContent(content);
        q.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }

    /**
     * 질문삭제
     * @param q
     */
    public void delete(Question q){
        this.questionRepository.delete(q);
    }

    /**
     * 추천기능
     * @param question
     * @param siteUser
     */
    public void vote(Question question, SiteUser siteUser){
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    private Specification<Question> search(String keyword) {
        return new Specification<>() {
            private static final long serialVersionUID = 123L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + keyword + "%"), // 제목
                        cb.like(q.get("content"), "%" + keyword + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + keyword + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + keyword + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + keyword + "%"));   // 답변 작성자
            }
        };
    }
}
