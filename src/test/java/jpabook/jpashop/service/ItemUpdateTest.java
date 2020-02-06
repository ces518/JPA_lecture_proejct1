package jpabook.jpashop.service;

import jpabook.jpashop.entities.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-06
 * Time: 23:10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest () {

        // 트랜잭션 커밋시 자동적으로 반영해준다
        // > 더티체킹
        Book book = em.find(Book.class, 1L);
        book.setName("1234");
    }
}
