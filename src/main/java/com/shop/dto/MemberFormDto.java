package com.shop.dto;
// 회원 가입 화면으로부터 넘어오는 가입정보를 담을 dto를 생성

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 해주세요.")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    // @NotEmpty: NULL 체크 및 문자열의 경우 길이 0인지 검사
    // @NotBlank: NULL 체크 및 문자열의 경우 길이 0 및 빈 문자열 ("") 검사
    // @Length(min=,max=): 최소, 최대 길이 검사
    // @Email: 이메일 형식인지 검사
    // @Max(숫자): 지정한 값보다 작은지 검사
    // @Min(숫자): 지정한 값보다 큰지 검사
    // @Null: 값이 NULL인지 검사
    // @NotNull : 값이 NULL이 아닌지 검사
}

