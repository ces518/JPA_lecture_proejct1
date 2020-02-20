package jpabook.jpashop.api;

import jpabook.jpashop.entities.Address;
import jpabook.jpashop.entities.Order;
import jpabook.jpashop.entities.OrderItem;
import jpabook.jpashop.entities.OrderStatus;
import jpabook.jpashop.repositories.OrderRepository;
import jpabook.jpashop.repositories.OrderSearch;
import jpabook.jpashop.repositories.order.query.OrderQueryDto;
import jpabook.jpashop.repositories.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-15
 * Time: 20:16
 **/
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1 () {
        List<Order> all = orderRepository.findAll(new OrderSearch());

        /* LAZY Loading */
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2 () {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<OrderDto> orderDtos = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return orderDtos;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3 () {
        List<Order> orders = orderRepository.findAllWithItem(new OrderSearch());
        List<OrderDto> orderDtos = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return orderDtos;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_1 (
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        // 아무런 설정을 하지 않은 경우에는 orderItems (컬렉션) 로 인해 n + 1 문제가 발생한다.
        // default_batch_fetch_size 를 설정하면, 해당 사이즈만큼 lazy loading 시 size 만큼 in Query로 가져온다.
        // V3방식은 한방쿼리이지만, 뻥튀기된 데이터의 용량이 모두 전송된다.
        // V3.1방식은 쿼리가 여러번 나가지만 정확하게 필요한 만큼의 데이터만 조회해 온다.
        // 두 방식은 상황에 따라 데이터의 양이 늘어날수록 전송하는 데이터의 양과, 쿼리횟수 사이에서 트레이드 오프를 해야한다.
        // -> 데이터 양이 많다면 V3.1방식이 성능이 더 잘 나올수 있다.
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> orderDtos = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return orderDtos;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4 () {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5 () {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order o) {
            this.orderId = o.getId();
            this.name = o.getMember().getName();
            this.orderDate = o.getOrderDate();
            this.orderStatus = o.getStatus();
            this.address = o.getDelivery().getAddress();
            this.orderItems = o.getOrderItems().stream()
                                                .map(orderItem -> new OrderItemDto(orderItem))
                                                .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
