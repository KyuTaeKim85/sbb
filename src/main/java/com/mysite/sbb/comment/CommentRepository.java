package com.mysite.sbb.comment;

import com.mysite.sbb.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
