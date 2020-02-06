package jpabook.jpashop.service;

import jpabook.jpashop.entities.item.Book;
import jpabook.jpashop.entities.item.Item;
import jpabook.jpashop.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-01-27
 * Time: 20:26
 **/

/**
 * 상품 리포지토리는 사실상 repository로 위임만 하는 역할을 한다.
 * 이런 경우에는 서비스를 따로 만들지 않고, Controller 에서 바로 호출하는것을 고려해보는것이 좋다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 메소드에 가까이 위치하는 @Transactional 설정이 우선권을 가진다.
    @Transactional
    public void saveItem (Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem (Long itemId, Book paramBook) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(paramBook.getPrice());
        findItem.setStockQuantity(paramBook.getStockQuantity());
    }

    public Item findOne (Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItems () {
        return itemRepository.findAll();
    }
}
