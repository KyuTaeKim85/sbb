package com.mysite.sbb.comment;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service    //해당 클랴스 객체는 모두 spring에서 관리할 수 있도록 서비스로 등록
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    CommentRepository commentRepository;
    /**
     * 댓글 생성하기
     * @param
     * @param content
     */
    public Comment create(Question question, String content, SiteUser author) {
        Comment c = new Comment();
        c.setContent(content);
        c.setCreateDate(LocalDateTime.now());
        c.setQuestion(question);
        c.setAuthor(author);
        c = this.commentRepository.save(c);
        return c;
    }

    public Comment getComment(Integer id) {
        Optional<Comment> optComment =  this.commentRepository.findById(id);
        if(optComment.isPresent()){
            return optComment.get();
        }else{
            throw new DataNotFoundException("question not found"); //사용자 정의 예외 객체 호출
        }
    }

    public Comment modify(Comment c, String content) {
        c.setContent(content);
        c.setModifyDate(LocalDateTime.now());
        c = this.commentRepository.save(c);
        return c;
    }

    public void delete(Comment c) {
        this.commentRepository.delete(c);
    }
}
