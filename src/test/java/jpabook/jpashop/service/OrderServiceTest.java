package jpabook.jpashop.service;

import jpabook.jpashop.entities.Address;
import jpabook.jpashop.entities.Member;
import jpabook.jpashop.entities.Order;
import jpabook.jpashop.entities.OrderStatus;
import jpabook.jpashop.entities.item.Book;
import jpabook.jpashop.entities.item.Item;
import jpabook.jpashop.exception.NotEnoughException;
import jpabook.jpashop.repositories.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;

    @Autowired OrderService orderService;

    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문 () throws Exception {
        //== given ==//
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        //== when ==//

        // 회원이 책 2권 주문 테스트
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //== then ==//
        Order getOrder = orderRepository.findById(orderId);
        assertEquals("상품 주문시 주문 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
    }

    @Test(expected = NotEnoughException.class)
    public void 재고수량_초과 () throws Exception {
        //== given ==//
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        int orderCount = 11;

        //== when ==//
        orderService.order(member.getId(), book.getId(), orderCount);

        //== then ==//
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소 () throws Exception {
        //== given ==//
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        //이번에는 취소에 대한 테스트 이기 때문에 주문까지 given에 존재한다.
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //== when ==//
        //테스트하고 싶은 항목이 when 절에 위치
        orderService.cancelOrder(orderId);

        //== then ==//
        Order getOrder = orderRepository.findById(orderId);
        assertEquals("주문 취소시 상태는 CANCEL 이 되어야 한다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStockQuantity());
    }

    // command + option + p 클릭시 파라메터 변수로 뺄수 있음
    private Item createBook(String name, int price, int stockQuantity) {
        // 책 세팅
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        // 회원 세팅
        Member member = new Member();
        member.setName("멤버1");
        member.setAddress(new Address("대전" ,"어딘가", "1234-1234"));
        em.persist(member);
        return member;
    }
}