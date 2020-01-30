package jpabook.jpashop.repositories;

import jpabook.jpashop.entities.OrderStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-01-30
 * Time: 22:45
 **/
@Getter @Setter
public class OrderSearch {

    private String memberName; // 회원명
    private OrderStatus orderStatus; // 주문상태: [ORDER, CANCEL]
}
