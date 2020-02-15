package jpabook.jpashop.repositories;

import jpabook.jpashop.entities.Address;
import jpabook.jpashop.entities.Order;
import jpabook.jpashop.entities.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-15
 * Time: 19:41
 **/
@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    // 식별자
    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name; // Lazy 초기화 (쿼리 발생)
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address; // Lazy 초기화 (쿼리 발생)
    }
}
