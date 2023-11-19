package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

// 우선 JpaRepository를 상속받는 CartRepository 인터페이스를 생성한다.
public interface CartRepository extends JpaRepository<Cart, Long> {


}
