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

    //== 생성 메서드 ==//
    public static OrderItem createOrderItem (Item item, int orderPrice, int count) {
        // orderPrice를 Item의 price를 참조하지않고 따로 받아오는 이유는 할인을 하는등의 상황등에 대처하기 위함
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 구매하는것 만큼 재고를 감소시킨다.
        item.removeStock(count);

        return orderItem;
    }

    //== 비즈니스 로직 ==//
    /**
     * 재고수량을 원복한다.
     */
    public void cancel() {
        // 재고수량을 원복한다.
        getItem().addStock(this.count);
    }

    //== 조회 로직 ==//

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
