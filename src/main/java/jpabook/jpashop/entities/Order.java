package jpabook.jpashop.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    //== 생성 메소드 ==//
    // 생성 메소드를 사용하는것이 중요함
    // 여기저기 비즈니스로직을 찾아다니지 않아도되고, 변경이 일어나더라도 한곳에서 관리가 된다.

    /**
     * 주문생성에 대한 비즈니스로직을 응집해 두는것
     */
    public static Order createOrder (Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        // 주문자 세팅
        order.setMember(member);
        // 딜리버리 세팅
        order.setDelivery(delivery);

        // 주문상품 세팅
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        // 주문상태: 주문으로 세팅
        order.setStatus(OrderStatus.ORDER);
        // 주문시간을 현재로 세팅
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직 ==//
    /**
     * 주문 취소
     * - 배송완료된 상품은 취소하지 못하다는 비즈니스로직
     */
    public void cancel () {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소할 수 없습니다.");
        }
        // 상태를 취소로 변경
        this.setStatus(OrderStatus.CANCEL);
        // 상품의 재고를 원복한다.
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //== 조회 로직 ==//
    // 뭔가 계산이 필요할때..

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice () {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        /*
        int totalPrice = 0;

        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
        */
    }
}
