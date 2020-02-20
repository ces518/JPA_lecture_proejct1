package jpabook.jpashop.repositories.order.query;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-20
 * Time: 20:02
 **/
@Data
public class OrderItemQueryDto {

    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
