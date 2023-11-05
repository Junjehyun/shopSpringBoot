package com.shop.constant;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

// 각각의 멤버는 일반 유저인지, 아니면 관리자인지 구분할 수 있는 역할이 있어야 한다. 이를 구분하기 위해서
// Role.java코드를 작성함.
public enum Role {

    // Role의 값으로 USER와 ADMIN 2개를 입력한다.
    USER, ADMIN
}

