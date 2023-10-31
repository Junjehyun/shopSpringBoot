package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;

@Entity // JPA에 엔티티 클래스라는걸 알려주는 것이다. 실제 DB 테이블과 매핑되는 핵심 클래스
@Table(name="item") // 어떤 테이블과 매핑 될지를 지정한다. item 테이블과 매핑 되도록 name을 item으로 지정
@Getter // Lombok?
@Setter // Lombok?
@ToString // Lombok?
public class Item {

    @Id // Entity로 선언한 클래스는 반드시 기본키를 가져야한다. 기본키가 되는 멤버 변수에 @Id를 붙임.
    @Column(name="item_id") // 그리고 테이블에 매핑될 컬럼의 이름을 @Column을 통해 설정한다. item 클래스의 id 변수와 item테이블의 item_id 컬럼이 매핑되도록 한다.
    @GeneratedValue(strategy = GenerationType.AUTO) // 마지막으로 @GeneratedValue를 통해 기본키 생성 전략을 AUTO로 지정 (설명은 하단)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50) // nullable 속성을 이용해서 항상 값이 있어야하는 필드는 not null을 설정한다. String필드는 디폴트 값으로 255가 설정 되어 있다. 각 String 필드마다 필요한 길이를 length 속성에 default값을 세팅한다.
    private String itemNm; // 상품명

    @Column(name="price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태 (ItemSellStatus 상수에서 가져옴)

    private LocalDateTime regTime; // 등록 시간

    private LocalDateTime updateTime; // 수정 시간

    // Item 클래스는 상품의 가장 기본적인 정보들을 담고 있다. 실제로는 1개의 상품에 여러가지 옵션 및
    // 옵션 상품의 가격, 재고, 배송 방법에 대한 정보까지 관리해야 하지만 최대한 단순한 형태로 만들어본다.
    // 상품 클래스 설계는 끝났다. Item 클래스를 엔티티로 매핑하기 위해서 관련된 어노테이션을 설정해야한다.

    // @Column 속성: 테이블을 생성할 때 컬럼에는 다양한 조건들이 들어간다. 예를들면 문자열을 저장하는
    // VARCHAR 타입은 길이를 설정할 수 있고, 테이블에 데이터를 넣을 때 데이터가 항상 존재해야 하는
    // Not Null 조건 등이 있다. @Column 어노테이션의 속성을 이용하면 테이블에 매핑되는 컬럼의 이름,
    // 문자열의 최대 저장 길이 등 다양한 제약 조건들을 추가할 수 있다.

    // @Entity: 클래스 상단에 입력하면 JPA에 엔티티 클래스라는걸 알려주는 것이다. Entity 클래스는 반드시
    // 기본키를 가져야 한다. @Id 어노테이션을 이용하여 id 멤버 변수를 상품 테이블의 기본키로 설정

    // @GeneratedValue 어노테이션을 통한 기본키를 생성하는 전략은 총 4가지다.
    // GenerationType.AUTO: JPA 구현체가 자동으로 생성 전략 결정 (디폴트값)
    // GenerationType.IDENTITY: 기본키 생성을 데이터베이스에 위임
    // GenerationType.SEQUENCE: 데이터베이스 시퀀스 오브젝트를 이용한 기본키 생성
    // GenerationType.TABLE: 키 생성용 테이블 사용. @TaableGenerator 필요

}
