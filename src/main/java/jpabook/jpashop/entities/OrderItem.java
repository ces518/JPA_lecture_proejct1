package jpabook.jpashop.entities;

import jpabook.jpashop.entities.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 14/01/2020
 * Time: 11:12 오후
 **/
@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id")
    private Item item;

    private int orderPrice; // 주문가격 (주문당시 가격)

    private int count; // 수량
}
