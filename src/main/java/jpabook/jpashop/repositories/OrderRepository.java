package jpabook.jpashop.repositories;

import jpabook.jpashop.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-01-29
 * Time: 22:42
 **/
@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    /**
     * 주문 기능
     * @param order
     */
    public void save (Order order) {
        em.persist(order);
    }

    /**
     * 주문 단건조회
     * @param orderId
     * @return
     */
    public Order findById (Long orderId) {
        return em.find(Order.class, orderId);
    }

    /**
     * 주문 검색기능 추후 구현
     */
    /*
    public List<Order> findAll (OrderSearch orderSearch) {
        return null;
    }
    */
}
