package jpabook.jpashop.repositories.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-20
 * Time: 19:59
 **/
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /*
        관심사를 분리하기 위해 Repository와 / Query (DTO로 조회, 화면종속적 인 것들)와 관련된 것은 따로 분리한다.
    * */

    // OrderQueryDto는 컨트롤러에 존재하는것을 참조하지 않음
    // Repository가 Controller를 참조하는 순환참조의 문제가 발생

    public List<OrderQueryDto> findOrderQueryDtos() {
        // JPQL은 new operation을 사용해도 컬렉션을 담을 수 없다.
        // 따라서 컬렉션을 제외한 데이터를 DTO로 조회한다.
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            // 1:N 관계기 때문에 별도의 쿼리를 질의 해야한다.
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    /**
     * OrderDto로 조회
     * @return
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repositories.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "from Order o " +
                "join o.member m " +
                "join o.delivery d ", OrderQueryDto.class)
        .getResultList();
    }

    /**
     * OrderItemDto로 조회
     * @param orderId
     * @return
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repositories.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
