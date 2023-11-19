package com.shop.config;


import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

        http
                .formLogin(form -> form
                                .loginPage("/members/login") // 로그인 페이지 url 설정
                                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL 설정
                                .usernameParameter("email") // 로그인 시 사용할 파라미터 이름으로 email지정
                                .failureUrl("/members/login/error") // 로그인 실패시 이동할 URL 지정
                            ).logout((logout) ->
                                logout.logoutRequestMatcher(new AntPathRequestMatcher
                                                    ("/members/logout"))
                                .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL설정
                );

        http
                // 시큐리티 처리에 HttpServletRequest를 이용한다는 것을 의미.
                .authorizeHttpRequests(authorize -> authorize
                // permitAll()을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정.
                // 메인 페이지, 회원 관련 URL, 뒤에서 만들 상품 상세 페이지, 상품 이미지를 불러오는 경로 등등.
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                // /admin으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우에만 접근 가능하도록 설정.
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 위에서 설정한 경로를 제외한 나머지 경로들은 모두 인증을 요구하도록 설정.
                .anyRequest().authenticated()
        );

        http
                // 인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록
                .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        );

         return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
// SecurityConfig소스를 직접작성한다. 현재는 모든 요청에 인증을 필요로하지만 SecurityConfig.java
// 의 configure 메소드에 설정을 추가하지 않으면 요청에 인증을 요구하지 않는다.
// URL에 따른 인증 및 인가 추가는 뒤에 예제에서 진행하겠습니다.