package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
     * @param q
     * @param content
     */
    public void create(String subject, String content){
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }
}
