package jpabook.jpashop.web;

import jpabook.jpashop.entities.Member;
import jpabook.jpashop.entities.Order;
import jpabook.jpashop.entities.item.Item;
import jpabook.jpashop.repositories.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-07
 * Time: 20:20
 **/
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm (Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items" ,items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order (@RequestParam Long memberId,
                         @RequestParam Long itemId,
                         @RequestParam int count) {

        // 엔티티를 조회해서 넘기는 것보다 식별자를 넘기는 이유 ?
        // 컨트롤러에서 찾으면 컨트롤러 로직이 지저분해지는 것도 있지만
        // 아이디값만 넘겨서 트랜잭션 내부에서 엔티티를 조회하는것이 테스트 작성시에도 좋음
        // 서비스계층에서 더 많은 것을 할수 있기 때문
        // 트랜잭션에 있는 서비스 계층의 비즈니스로직에서 사용될때 누릴수 있는 혜택이 많음
        orderService.order(memberId, itemId, count);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList (@ModelAttribute OrderSearch orderSearch, Model model) {

        // 단순하게 화면에서 조회하는 기능이라면
        // 서비스 계층을 사용하는것보다 리포지토리에서 바로 호출하는것도 나쁘지 않다.
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder (@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
