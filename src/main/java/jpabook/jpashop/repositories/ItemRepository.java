package jpabook.jpashop.repositories;

import jpabook.jpashop.entities.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-01-27
 * Time: 20:16
 **/
@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 아이템 저장
     * @param item
     */
    public void save (Item item) {
        if (item.getId() == null) {
            // 최초 저장시 새로이 등록
            em.persist(item);
        } else {
            // 기존에 존재하는 엔티티를 업데이트(실제 업데이트는 아니지만 비슷한 느낌이다.)
            em.merge(item);
        }
    }

    /**
     * 아이템 단건 조회
     * @param id
     * @return
     */
    public Item findOne (Long id) {
        return em.find(Item.class, id);
    }

    /**
     * 아이템 목록 조회
     * @return
     */
    public List<Item> findAll () {
        return em.createQuery("select i from Item i")
                .getResultList();
    }
}
