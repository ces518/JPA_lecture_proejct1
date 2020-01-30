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
}
