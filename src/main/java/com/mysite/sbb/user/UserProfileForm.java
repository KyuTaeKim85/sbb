package com.mysite.sbb.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileForm {
    @Size(min = 8, max = 20)
    @NotEmpty(message = "기존 비밀번호는 필수항목입니다.")
    private String oldpassword;

    @Size(min = 8, max = 20)
    @NotEmpty(message = "새 비밀번호는 필수항목입니다.")
    private String password1;

    @Size(min = 8, max = 20)
    @NotEmpty(message = "새 비밀번호 확인은 필수항목입니다.")
    private String password2;
}
