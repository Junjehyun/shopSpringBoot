package com.shop.config;
// 현재는 모든 요청에 인증을 필요로 하지만 SecurityConfig.java의 configure 메소드에 설정을
// 추가하지 않으면 요청에 인증을 요구하지 않는다. URL에 따른 인증 및 인가 추가는 나중에 진행

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
// WebSecurituConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 어노테이션을 선언하면 SpringSecurityFilterChain
// 이 자동으로 포함된다. WebSecurityConfigurerAdapter를 상속받아서 메소드 오버라이딩을 통해 보안 설정을 커스터마이징한다.
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form.loginPage("/members/login") // 로그인 페이지 url설정
                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL을 설정
                .usernameParameter("email") // 로그인 시 사용할 파라미터 이름으로 email지정
                .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL 설정
                ).logout((logout) ->
                logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL설정
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL설정
        );
        return http.build();
    }

    @Bean
    // 비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 회원 정보가 그대로 노출된다.
    // 이를 해결하기 위해 BCryptPasswordEncoder의 해시 함수를 이용하여 비밀번호를 암호화 하여 저장한다.
    // BCryptPasswordEncoder를 빈으로 등록하여 사용하겠다.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

