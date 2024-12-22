package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")    //해당 컨트롤러를 호출 시에 url앞에 question에 있어야만 호출되도록 셋팅
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    //브라우저에서 아래 입력한 주소로 접근하게 되면 list메소드가 실행
    @GetMapping("/list")
    //@ResponseBody
    public String list(Model model){ // Model은 템플릿에서 지원하는 라이브러리

        /*
        String frd = "20241208000013";
        String tod = "20241208154713";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime frddateTime = LocalDateTime.parse(frd, formatter);
        LocalDateTime todateTime = LocalDateTime.parse(tod, formatter);

        System.out.println("frddateTime:"+frddateTime);
        System.out.println("todateTime:"+todateTime);

        List<Question> questionList = this.questionRepository.findByCreateDateBetween(frddateTime, todateTime);*/
        //List<Question> questionList = questionRepository.findBySubjectOrContent("안녕하세요?", "나는 김규태입니다1.");

        List<Question> questionList = questionService.getList();
        //ui html에서 questionList를 호출하면 List<Question>객체를 가져옴
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        System.out.println("id:"+id);

        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate() {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(Model model, @RequestParam("subject") String subject, @RequestParam("content") String content){

        System.out.println("subject:"+subject);
        System.out.println("content:"+content);
        //실체 질문 등록하도록 서비스 함수 호출
        this.questionService.create(subject, content);

        //redirect는 GET호출
        return "redirect:/question/list";
    }
}
