package com.mysite.sbb.answer;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserService userService;

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    /**
     * 답변 생성하기
     * @param q
     * @param content
     */
    public Answer create(Question q, String content, SiteUser siteUser){
        Answer a = new Answer();
        a.setQuestion(q);
        a.setContent(content);
        a.setCreateDate(LocalDateTime.now());
        a.setAuthor(siteUser);
        this.answerRepository.save(a);  //answer primary key 인 id가 만들어짐.

        return a;
    }


    /**
     * 답변수정하기
     * @param a
     * @param content
     */
    public void modify(Answer a, String content){
        a.setContent(content);
        a.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }

    /**
     * 답변삭제하기
     * @param a
     */
    public void delete(Answer a){
        this.answerRepository.delete(a);
    }

    /**
     * 추천기능
     * @param answer
     * @param siteUser
     */
    public void vote(Answer answer, SiteUser siteUser){
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}
