package com.shop.controller;


import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller

// 클라이언트의 요청에 대해서 어떤 컨트롤러가 처리할지 매핑하는 어노테이션이다. url에 "/thymeleaf/" 경로로 오는 요청을
// ThymeleafExController가 처리하도록 한다.
@RequestMapping(value="/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model) {
        // Model 객체를 이용해 뷰에 전달한 데이터를 key, value 구조로 넣어준다.
        model.addAttribute("data", "타임리프 예제 입니다.");
        // templates 폴더를 기준으로 뷰의 위치와 이름(thymeleafEx01.html)을 반환
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value = "/ex02")
    public String thymeLeafExample02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
        // 전달받은 itemDto객체를 th:text를 이용하여 출력
    }

    @GetMapping (value = "/ex03")
        public String thymeleafExample03(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        // 반복문을 통해 화면에서 출력할 10개의 itemDto 객체를 만들어서 itemDtoList에 넣어줌.
        for (int i=1; i<=10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품1" + i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        // 화면에서 출력할 itemDtoList를 model에 담아서 View에 전달한다.
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
        }

        @GetMapping(value = "/ex04")
        public String thymeleafExample04(Model model) {
            List<ItemDto> itemDtoList = new ArrayList<>();

            for(int i=1; i<10; i++) {
                ItemDto itemDto = new ItemDto();
                itemDto.setItemDetail("상품 상세 설명" + i);
                itemDto.setItemNm("테스트 상품" + i);
                itemDto.setPrice(1000*i);
                itemDto.setRegTime(LocalDateTime.now());

                itemDtoList.add(itemDto);
            }

            model.addAttribute("itemDtoList", itemDtoList);
            return "thymeleafEx/thymeleafEx04";
        }

        // th:href를 이용한 링크 처리용 컨트롤러
        @GetMapping(value = "/ex05")
        public String thymeleafExample05() {
            return "thymeleafEx/thymeleafEx05";
        }

        // th:href를 이용한 파라미터 데이터 전달용 컨트롤러 작성하기
    @GetMapping(value = "/ex06")
    // 전달했던 매개 변수와 같은 이름의 String 변수 param1, param2를 파라미터로 설정하면
    // 자동으로 데이터가 바인딩된다. 매개 변수를 model에 담아서 viw로 전달)
    public String thymeleafExample06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
            return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07() {
        return "thymeleafEx/thymeleafEx07";
    }

}
