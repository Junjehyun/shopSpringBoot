package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    // 회원은 이메일을 통해 유일하게 구분해야 하기 때문에, 동일한 값이 데이터베이스에 들어올 수 없도록 unique 속성을 지정
    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    // 자바의 enum 타입을 엔티티의 속성으로 지정. Enum을 사용할 때 기본적으로 순서가 저장되는데, enum의 순서가 바뀔 경우
    // 문제가 발생할 수 있으므로 "EnumType.STRING"옵션을 사용해서 String으로 저장하기를 권장.
    @Enumerated(EnumType.STRING)
    private Role role;

    // Member 엔티티를 생성하는 메소드다. Member 엔티티에 회원을 생성하는 메소드를 만들어서 관리를 한다면
    // 코드가 변경되더라도 한 군데만 수정하면 되는 이점이 있다.
    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        // 스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean을 파라미터로 넘겨서 비밀번호를 암호화함.
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        // Member 엔티티 생성 시 User Role로 생성하던 권한을 ADMIN Role로 생성하도록 수정한다.
        member.setRole(Role.ADMIN);
        return member;
    }

}
// 이제 회원 정보를 저장하는 Member 엔티티를 만든다. 관리할 회원 정보는 이름, 이메일, 비밀번호, 주소, 역할이다.
