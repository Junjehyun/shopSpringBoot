package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @OneToOne 어노테이션을 이용해 회원 엔티티와 일대일로 매핑을 한다.
    @OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn 어노테이션을 이용해 매핑할 외래키를 지정한다. name 속성에는 매핑할 외래키의 이름을
    // 설정한다. @JoinColumn의 name을 명시하지 않으면 JPA가 알아서 ID를 찾지만, 컬럼명이 원하는 대로
    // 생성되지 않을 수 있기 때문에 직접 지정한다.
    @JoinColumn(name = "member_id")
    private Member member;
}
