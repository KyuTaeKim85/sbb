package com.mysite.sbb.user;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final QuestionService questionService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 폼으로 이동
     * @param userCreateForm
     * @return
     */
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    /**
     * 회원가입 실제로 하기
     * @param userCreateForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        // 중복회원 가입 및 기타 에러 처리
        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch(DataIntegrityViolationException e) { // username값이 값을 경우 에러 처리
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    /**
     * 로그인 폼 화면으로 이동
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "login_form";
    }

    /**
     * 내정보 폼 화면으로 이동
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(UserProfileForm userProfileForm, Model model, Principal principal
            , @RequestParam(value="question-page", defaultValue="0") int questionPage) {
        SiteUser siteUser = this.userService.getUser(principal.getName());

        model.addAttribute("username", siteUser.getUsername());
        model.addAttribute("userEmail", siteUser.getEmail());

        Page<Question> writeQuestions = this.questionService.getListByAuthor(questionPage, siteUser);
        model.addAttribute("write_question_paging", writeQuestions);

        return "profile";
    }

    /**
     * 내 정보 저장 하기
     * @param userProfileForm
     * @param
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profileup")
    public String profileup(UserProfileForm userProfileForm, Model model, Principal principal, BindingResult bindingResult) {
        SiteUser siteUser = this.userService.getUser(principal.getName());

        System.out.println("dbpassword" + siteUser.getPassword());
        System.out.println("getOldpassword" + userProfileForm.getOldpassword());
        System.out.println("getPassword1" + userProfileForm.getPassword1());
        System.out.println("getPassword2" + userProfileForm.getPassword2());

        Page<Question> writeQuestions = this.questionService.getListByAuthor(0, siteUser);
        model.addAttribute("write_question_paging", writeQuestions);

        if (bindingResult.hasErrors()) {
            return "profile";
        }

        //평문, 암호문
        boolean chk = passwordEncoder.matches(userProfileForm.getOldpassword(), siteUser.getPassword());
        System.out.println("chk" + chk);
        if(chk==false){
            bindingResult.rejectValue("oldpassword", "passwordInCorrect",
                    "기존 패스워드가 일치하지 않습니다.");
            return "profile";
        }

        if (!userProfileForm.getPassword1().equals(userProfileForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "profile";
        }

        try {
            userService.update(siteUser, userProfileForm.getPassword1());
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("updateFailed", e.getMessage());
        }

        return "redirect:/";
    }

}