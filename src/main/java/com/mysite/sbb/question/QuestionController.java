package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/question")    //해당 컨트롤러를 호출 시에 url앞에 question에 있어야만 호출되도록 셋팅
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    //브라우저에서 아래 입력한 주소로 접근하게 되면 list메소드가 실행
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    //@ResponseBody
    public String list(HttpSession session, Model model,
                       @RequestParam(value="page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String keyword,
                       Principal principal){ // Model은 템플릿에서 지원하는 라이브러리

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

        /*List<Question> questionList = questionService.getList();
        //ui html에서 questionList를 호출하면 List<Question>객체를 가져옴
        model.addAttribute("questionList", questionList);*/

        Page<Question> paging = this.questionService.getList(page, keyword);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", keyword);

        if(principal != null){
            SiteUser siteUser = this.userService.getUser(principal.getName());
            System.out.println("getUsername:"+siteUser.getUsername());
            session.setAttribute("username", siteUser.getUsername());
        }
        //model.addAttribute("username", siteUser.getUsername());

        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        System.out.println("id:"+id);

        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {

        return "question_form";
    }

    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        SiteUser siteUser = userService.getUser(principal.getName());

        //SiteUser siteUser = this.userService.getUser(principal);
        //실체 질문 등록하도록 서비스 함수 호출
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

        //redirect는 GET호출
        return "redirect:/question/list";
    }

    /**
     * 질문수정하기 폼으로 이동
     * @param questionForm
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal){

        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        return "question_form";
    }

    /**
     * 질문 수정하기
     * @param questionForm
     * @param id
     * @param bindingResult
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, @PathVariable("id") Integer id, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }

        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        //실제수정
        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        return "redirect:/question/detail/"+id;
    }

    /**
     * 질문삭제하기
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Integer id, Principal principal){
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        //실제삭제
        questionService.delete(question);

        return "redirect:/";        //게시판 리스트로 이동
    }

    /**
     * 추천기능
     * @param principal
     * @param id
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        
        //실제 추전
        this.questionService.vote(question, siteUser);
        
        return String.format("redirect:/question/detail/%s", id);
    }
}
