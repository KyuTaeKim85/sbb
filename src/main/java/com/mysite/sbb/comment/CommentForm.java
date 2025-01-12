package com.mysite.sbb.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {
    @NotEmpty(message = "내용은 필수항목입니다.") // empty 혹은 null 일때 프론트에 메시지 전달
    private String content; // 질문 내용 항목
}
