package jpabook.jpashop.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 14/01/2020
 * Time: 11:03 오후
 **/
@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    /*
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
     */
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태: ORDER, CANCEL

    /* 연관관계 편의 메소드
    *  연관관계 편의 메소드의 위치는 핵심 적인 엔티티에 두는것이 좋다.
    * */
    public void setMember (Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem (OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery (Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
