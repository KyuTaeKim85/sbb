package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private  final AnswerRepository answerRepository;

    /**
     * 답변 생성하기
     * @param q
     * @param content
     */
    public void create(Question q, String content){
        Answer a = new Answer();
        a.setQuestion(q);
        a.setContent(content);
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }
}
