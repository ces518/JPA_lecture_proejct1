package jpabook.jpashop.api;

import jpabook.jpashop.entities.Order;
import jpabook.jpashop.repositories.OrderRepository;
import jpabook.jpashop.repositories.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 컬렉션이 아닌 관계
 * xToOne
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * `문제점`
     * 1.엔티티를 그대로 리턴하면, 무한 루프에 걸린다. (양방향 연관관계시)
     * -> 한쪽을 @JsonIgnore로 json 변환대상에서 제외시켜주어야함 (모든 양방향 연관관계)
     * 2.SimpleType.. 예외가 발생한다.
     * -> Lazy Loading 설정이 되어있을경우 실제 엔티티가 아닌 프록시 객체를 가지고 있다. (하이버네이트는 byteBuddy 사용)
     * -> json으로 변환을 하지 못함
     * -> Hibernate5Module 를 빈으로 등록 해야한다.
     * -> 즉시로딩으로 변경해서도 안된다.
     *
     * * API 스펙에 그대로 노출하면 문제가 많다.
     * -> 반드시 필요한 데이터만 API를 통해 노출할것
     * * 성능상 문제도 있다.
     *
     * @return
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1 () {
        List<Order> all = orderRepository.findAll(new OrderSearch());

        // Hibernate5Module 을 사용하지않고 초기화 하는 방법
        for (Order order : all) {
            order.getMember().getName(); // Lazy Loading 강제 초기화
            order.getDelivery().getAddress(); // Lazy Loading 강제 초기화
        }
        return all;
    }
}
