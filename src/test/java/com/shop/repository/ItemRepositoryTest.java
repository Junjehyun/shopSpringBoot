package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 통합 테스트를 위해 스프링 부트에서 제공하는 어노테이션이다. 실제 어플리케이션을 구동할 때 처럼 모든 Bean을 IoC 컨테이너에 등록한다.

@TestPropertySource(locations = "classpath:application-test.properties")// 테스트 코드 실행 시 application.properties에
    // 설정해둔 값 보다 application-test.properties에 같은 설정이 있다면 더 우선순위를 부여한다. 기존에는 MySQL을 사용했지만 테스트 코드 실행 시에는 H2데이터베이스를 사용하게 된다.

class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository; // ItemRepository를 사용하기 위해서 @Autowired 어노테이션을 이용해 Bean을 주입한다.

    @Test // Test할 메소드 위에 선언하여 해당 메소드를 테스트 대상으로 지정하는것.
    @DisplayName("상품 저장 테스트") // Junit5에 추가된 어노테이션으로 테스트 코드 실행 시 @DisplayName에 지정한 테스트명이 노출된다.

    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    // 테스트 코드 실행 시 데이터베이스에 상품 데이터가 없으므로 테스트 데이터 생성을 위해서 10개의 상품을 저장하는 메소드를
    // 작성하여 findByItemNmTest()에서 실행해줌
    public void createItemList() {
        for(int i=1; i<10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        // ItemRepository 인터페이스에 작성했던 findBYItemNm 메소드를 호출한다. 파라미터로는 "테스트 상품1" 이라는
        // 상품명을 전달한다.
        List<Item> itemLIst = itemRepository.findByItemNm("테스트 상품1");
        for(Item item : itemLIst) {
            System.out.println(item.toString()); // 조회 결과 얻은 item 객체들을 출력
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemOrItemDetailTest() {
        this.createItemList(); // 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어준다.
        List<Item> itemList =
                itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
                // 상품명이 "테스트 상품1" 또는 상품 상세 설명이 "테스트 상품 상세 설명5"이면 해당 상품을 itemList에 할당
                // 테스트 코드를 실행하면 조건대로 2개의 상품이 출력 되는걸 볼 수 있다.
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

}