package com.mysite.sbb.question;

// 질문을 등록하기 위한 테이터를 받을 항목들의 집합 클래스

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message = "제목은 필수항목입니다.") // empty 혹은 null 일때 프론트에 메시지 전달
    @Size(max=10)          //최대 200자 이상은 넣을 수 없음.
    private String subject; // 질문 등록 항목

    @NotEmpty(message = "내용은 필수항목입니다.") // empty 혹은 null 일때 프론트에 메시지 전달
    private String content; // 질문 내용 항목
}
