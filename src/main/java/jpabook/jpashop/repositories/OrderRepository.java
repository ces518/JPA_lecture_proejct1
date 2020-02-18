package jpabook.jpashop.repositories;

import jpabook.jpashop.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

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
    public List<Order> findAll (OrderSearch orderSearch) {


        // 아래 쿼리는 값이 다 있다는 가정..
        List<Order> orders = em.createQuery("select o from Order o join o.member m " +
                "where o.status = :status " +
                "and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) // 최대 1000건
                .getResultList();

        //== Criteria 사용 방법 ==//
        // JPA 표준
        // 실무에서 사용하기 어렵다.
        // 단점: 유지보수성이 거의 0에 가깝다.
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        if (StringUtils.hasLength(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(criteria.toArray(new Predicate[criteria.size()]));
        orders = em.createQuery(cq)
                .setMaxResults(1000)
                .getResultList();
        //== ==//
        return orders;
    }

    /**
     * 한방 쿼리로 멤버와, 딜리버리를 함께 조회한다.
     * @return
     */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d", Order.class)
                .getResultList();
    }

    /**
     * 페이징 쿼리
     * @return
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        // ToOne 관계도 In Query로 최적화가 되지만, 네트워크를 더 타기때문에 ToOne관계는 fetch join시 명시 해 두는것이 좋음
        return em.createQuery("select o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    // 엔티티나 값타입만 반환이 가능하다.
    // DTO로 반환하려면 new Operation을 사용해야한다.
    // 재사용성이 떨어진다.
    // DTO로 조회했기때문에 비즈니스 로직 등을 활용할 수 없다.
    public List<OrderSimpleQueryDto> findOrderDtos() {
        // JPQL에서 엔티티인 o 를 넘기면 엔티티가 아닌 식별자가 넘어가기때문에 필드를 하나하나 지정해 주어야한다..
        return em.createQuery(
                "select new jpabook.jpashop.repositories.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }

    /*
        Order와 OrderItem join시 중복된 데이터가 발생한다.
        JPA에서 Order 데이터가 뻥튀기 되어 버린다.
        > fetch join 은 DB 입장에서 select절에 데이터를 추가해주냐 마냐 차이일뿐 결국 join sql을 사용한다.
    * */
    public List<Order> findAllWithItem(OrderSearch orderSearch) {
        // distinct 키워드를 사용하면 DB distinct + JPA 에서 중복 엔티티를 제거한다.
        return em.createQuery(
                "select distinct o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d " +
                        "join fetch o.orderItems oi " +
                        "join fetch o.item i", Order.class)
                .getResultList();
    }
}
