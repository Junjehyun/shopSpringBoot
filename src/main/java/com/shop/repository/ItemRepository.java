package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JpaRepository를 상속 받는 ItemRepository를 작성했다. JpaRepository는 2개의 제네릭 타입을 사용하는데,
// 첫 번째는 엔티티 타입 클래스를 넣어주고, 두 번째는 기본키 타입을 넣어준다. Item 클래스는 기본키 타입이 Long이므로
// Long을 넣어준다. JpaRepository는 기본적은 CRUD 및 페이징 처리를 위한 메소드가 정의 되어 있다.
// 예제에서 작성할 테스트 코드는 엔티티를 저장하는 save() 메소드다.
public interface ItemRepository extends JpaRepository<Item, Long>,
QuerydslPredicateExecutor<Item> // QueryDslPredicateExecutor 인터페이스 상속을 추가

{

    List<Item> findByItemNm(String itemNm);
    // itemNm(상품명)으로 데이터를 조회하기 위해서 By 뒤에 필드명인 ItemNm을 메소드의 이름에 붙여준다.
    // 엔티티명은 생략이 가능하므로 findItemByItemNm 대신에 findByItemNm으로 메소드명을 만든다.
    // 매개 변수로는 검색할 때 사용할 상품명 변수를 넘겨준다.
    // 기존에 작성했던 ItemRepositoryTest 클래스에 테스트 코드를 추가해보자.

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    // 상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리 메소드.

    List<Item> findByPriceLessThan(Integer price);
    // 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드.

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer Price);

    // @Query를 이용한 검색 처리하기
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    // @Query 어노테이션 안에 JPQL로 작성한 쿼리문을 넣어준다. from 뒤에는 엔티티 클래스로 작성한 Item을 지정했고,
    // Item으로부터 데이터를 Select 하겠다는 것을 의미한다.

    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    // 파라미터에 @Param 어노테이션을 이용하여 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정할 수 있음.
    // 현재는 itemDetail변수를 "like % %" 사이에 ":ItemDetail"로 값이 들어가도록 작성했다.

    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc"
    , nativeQuery = true) // value안에 네이티브 쿼리문을 작성하고 "nativeQuery=true"를 지정한다.
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
