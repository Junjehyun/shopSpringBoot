package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping(value = "/new")
    // 회원 가입 페이지로 이동할 수 있도록 MemberController 클래스에 메소드를 작성한다.
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "/member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(MemberFormDto memberFormDto) {
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/";
    }

    // 회원 가입이 성공하면 메인페이지로 리다이렉트 시켜주고, 회원 정보 검증 및 중복회원 가입 조건에 의해 실패한다면
    // 다시 회원 가입 페이지로 돌아가 실패 이유를 화면에 출력해준다.
    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        // 검증하려는 객체의 앞에 @Valid 어노테이션을 선언하고, 파라미터로 bindingResult 객체를 추가한다. 검사 후 결과는
        // bindingResult에 담아준다. bindingResult.hasErrors()를 호출하여 에러가 있다면 회원 가입 페이지로 이동한다.
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            // 회원 가입시 중복 회원 가입 예외가 발생하면 에러 메세지를 뷰로 전달한다.
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
            return "redirect:/";
        }
    }

