package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserController;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")    //해당 컨트롤러를 호출 시에 url앞에 question에 있어야만 호출되도록 셋팅
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    @PostMapping("/create/{id}")    //id는 질문 게시판 번호
    public String createAnswer(
            Model model,
            @PathVariable("id") Integer id,
            @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal
    ){
        Question question = this.questionService.getQuestion(id);
        System.out.println("question:"+question);

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        SiteUser siteUser = userService.getUser(principal.getName());

        //실체 답변 등록하도록 서비스 함수 호출
        //TODO: 답변을 저장한다.
        Answer answer = this.answerService.create(question, answerForm.getContent(), siteUser);

        //redirect는 GET호출
        return String.format("redirect:/question/detail/%s#answer_%s", id, answer.getId());
    }

    /*@PostMapping("/create/{id}")    //id는 질문 게시판 번호
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
    }*/

    /**
     * 답변수정 폼 이동하기
     * @param answerForm
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    /**
     * 답변수정하기
     * @param answerForm
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, @PathVariable("id") Integer id, BindingResult bindingResult,Principal principal){

        if(bindingResult.hasErrors()){
            return "question_form";
        }

        Answer answer = this.answerService.getAnswer(id);

        //실제수정
        answerService.modify(answer, answerForm.getContent());

        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    /**
     * 답변삭제하기
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()") //로그인(인증)한 사람만 해당 메소드를 실행
    @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Integer id, Principal principal){

        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        //실제삭제
        answerService.delete(answer);

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
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        //실제 추전
        this.answerService.vote(answer, siteUser);

        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
}
