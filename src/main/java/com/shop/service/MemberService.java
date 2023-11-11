package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
// 비지니스 로직을 담당하는 서비스 계층 클래스에 @Transcational 어노테이션을 선언한다. 로직을 처리하다가
// 에러가 발생했다면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜준다.
@Transactional
// 빈을 주입하는 방법으로는 @Autowired 어노테이션을 사용하거나, 필드 주입(Setter 주입), 생성자 주입을
// 이용하는 방법이 있다. @RequiredArgsConstructor 어노테이션은 final이나 @NonNull이 붙은 필드에 생성자
// 를 생성해준다. Bean에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하다면 @Autowired 어
// 노테이션 없이 의존성 주입이 가능하다.
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    // MemberService가 UserDetailsService를 구현한다.
    // UserDetailService 인터페이스는 데이터베이스에서 회원 정보를 가져오는 역할을 담당한다.

    private final MemberRepository memberRepository;

    // 이미 가입된 회원의 경우 IllegalStateException 예외를 발생시킨다.
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws
            // UserDetailService 인터페이스의 loadUserByUsername()메소드를 오버라이딩 한다.
            // 로그인할 유저의 email을 파라미터로 전달받는다.
            UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        // UserDetail을 구현하고 있는 User 객체를 반환해준다. User 객체를 생성하기 위해서 생성자로
        // 회원의 이메일, 비밀번호, role을 파라미터가 넘겨준다.
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
