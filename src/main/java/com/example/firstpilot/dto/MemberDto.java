package com.example.firstpilot.dto;

import com.example.firstpilot.model.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@NoArgsConstructor
public class MemberDto {
    private Long memberId;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]{6,12}$", message = "영문, 숫자를 포함하여 6~12자리의 비밀번호를 입력해주세요.")
    private String password;

    private String updatedDate;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .updatedDate(updatedDate)
                .build();
    }

    public MemberDto(Long memberId, String nickname, String updatedDate) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.updatedDate = updatedDate;
    }

}
