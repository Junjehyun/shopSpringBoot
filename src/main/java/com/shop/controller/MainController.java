package com.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;

// 회원가입 후 메인 페이지로 갈 수 있도록 MainController에 소스 작성.
public class MainController {

    @GetMapping(value="/")
    public String main() {
        return "main";
    }
}
