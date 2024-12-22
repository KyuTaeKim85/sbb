package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/answer")    //해당 컨트롤러를 호출 시에 url앞에 question에 있어야만 호출되도록 셋팅
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")    //id는 질문 게시판 번호
    public String createAnswer(
            Model model,
            @PathVariable("id") Integer id,
            @RequestParam("content") String content
    ){
        Question question = this.questionService.getQuestion(id);
        System.out.println("question:"+question);
        //실체 답변 등록하도록 서비스 함수 호출
        //TODO: 답변을 저장한다.
        this.answerService.create(question, content);
        
        //redirect는 GET호출
        return String.format("redirect:/question/detail/%s", id);
    }
}
