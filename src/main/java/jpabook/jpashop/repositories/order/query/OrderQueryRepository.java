package jpabook.jpashop.repositories.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 최적화 버전
     * - > 쿼리 두번으로 최적화 된다.
     * @return
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        // orderId 목록으로 추출
        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        // orderItems 세팅
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        // inQuery로 OrderItem 목록 조회
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repositories.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();


        // OrderId 를 기반으로 Map으로 변환
        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                        .map(o -> o.getOrderId())
                        .collect(Collectors.toList());
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
