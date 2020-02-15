package jpabook.jpashop.api;

import jpabook.jpashop.entities.Address;
import jpabook.jpashop.entities.Order;
import jpabook.jpashop.entities.OrderStatus;
import jpabook.jpashop.repositories.OrderRepository;
import jpabook.jpashop.repositories.OrderSearch;
import jpabook.jpashop.repositories.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


    /**
     * DTO로 변환하여 응답
     *
     * `문제점`
     * -> V1과 마찬가지로 Lazy Loading 으로 인한 쿼리가 너무 많이 나간다.
     *      -> N + 1 문제 발생
     *      -> fetchType EAGER 로 바꿔도 최적화 되지 않는다.
     *      -> 오히려 쿼리 예측이 더 힘들어진다.
     *
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2 () {
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        /* DTO로 변환 */
        List<SimpleOrderDto> orderDtos = orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());
        return orderDtos;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3 () {
        /* fetchJoin 사용 */
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        /* DTO로 변환 */
        List<SimpleOrderDto> orderDtos = orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());
        return orderDtos;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4 () {
        return orderRepository.findOrderDtos();
    }

    /**
     * API 스펙을 명확하게 정의해야 한다.
     */
    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); // Lazy 초기화 (쿼리 발생)
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); // Lazy 초기화 (쿼리 발생)
        }
    }
}
