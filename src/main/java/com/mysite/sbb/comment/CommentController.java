package com.mysite.sbb.comment;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/create/question/{id}")
    public String create(CommentForm commentForm) {
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/question/{id}")
    public String create(@Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal){

        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Comment c = this.commentService.create(question, commentForm.getContent(), siteUser);

        return String.format("redirect:/question/detail/%s", c.getQuestionId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyComment(CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);

        if(!comment.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentForm.setContent(comment.getContent());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal,
                                @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        comment = this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/question/detail/%s", comment.getQuestionId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/question/detail/%s", comment.getQuestionId());
    }
}
