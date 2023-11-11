package com.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {
    // @NotEmpty: NULL체크 및 문자열의 경우 0인지 검사
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    // @Email: 이메일 형식인지 검사
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    // @Length(min,max): 최소, 최대길이 검사
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;
}
// 회원 가입 화면으로부터 넘어오는 가입정보를 담을 dto를 생성

// 변수 위 어노테이션은 유효성을 검증할 클래스의 필드에 어노테이션을 선언한 것이다.