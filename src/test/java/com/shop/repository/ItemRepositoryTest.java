package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 통합 테스트를 위해 스프링 부트에서 제공하는 어노테이션이다. 실제 어플리케이션을 구동할 때 처럼 모든 Bean을 IoC 컨테이너에 등록한다.

@TestPropertySource(locations = "classpath:application-test.properties")// 테스트 코드 실행 시 application.properties에
    // 설정해둔 값 보다 application-test.properties에 같은 설정이 있다면 더 우선순위를 부여한다. 기존에는 MySQL을 사용했지만 테스트 코드 실행 시에는 H2데이터베이스를 사용하게 된다.

class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em; // 영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입

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

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        // 현재 데이터베이스에 저장된 가격은 10001~10010 이다. 테스트 코드 실행시 10개의 상품을 저장하는 로그가
        // 콘솔에 나타나고 맨 마지막에 가격이 10005보다 작은 4개의 상품이 출력되는걸 확인할 수 있다.
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // JPAQueryFactory를 이용하여 쿼리를 동적으로 생성한다. 생성자의 파라미터로는 EntityManager 객체를 넣어준다. (em)
        QItem qItem = QItem.item;
        // Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용.
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                // 자바 소스코드지만, SQL문과 비슷하게 소스를 작성할 수 있다.
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();
        // JPAQuery 메소드중 하나인 fetch를 이용해서 쿼리 결과를 리스트로 반환한다. fetch() 메소드 실행 시점에
        // 쿼리문이 실행된다.

        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    // 상품 데이터를 만드는 새로운 메서드를 하나 만든다. 1번부터 5번 상품은 상품의 판매 상태를 SELL(판매중)으로 지정,
    // 6번부터 10번 상품은 판매상태를 SOLD_OUT (품절)으로 세팅해 생성한다.
    public void createItemList2() {
        for(int i=1; i<5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for (int i=6; i<=10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트2")
    public void queryDslTest2() {

        this.createItemList2();

        // BooleanBuilder는 쿼리에 들어갈 조건을 만들어주는 빌더라고 생각하면 된다. Predicate를 구현하고 있으며
        // 메소드 체인 형식으로 사용할 수 있다.
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        // 필요한 상품을 조회하는데 필요한 "and" 조건을 추가하고 있다. 아래의 소스에서 상품의 판매상태가 SELL일때만
        // booleanBuilder에 판매상태 조건을 동적으로 추가하는 것을 볼 수 있다.
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        // 데이터를 페이징해 조회하도록 PageRequest.of()메소드를 이용해 Pageble 객체를 생성한다.
        // 첫 번째 인자는 조회할 페이지의 번호, 두 번째 인자는 한 페이지당 조회할 데이터의 개수를 넣어준다.
        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult =
                // QueryDslPredicateExecutor 인터페이스에서 정의한 findAll() 메소드를 이용해 조건에 맞는 데이터를
                // Page 객체로 받아옵니다.
                itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " +
                itemPagingResult. getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList) {
            System.out.println(resultItem.toString());
        }
    }

}