package jpabook.jpashop.web;

import jpabook.jpashop.entities.item.Book;
import jpabook.jpashop.entities.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-06
 * Time: 22:34
 **/
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm (Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create (BookForm form) {

        // setter를 모두 닫고, 생성자 메소드를 사용하는것이 좋은 설계임.
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list (Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm (@PathVariable Long itemId, Model model) {
        // 캐스팅하는것이 좋지는 않지만 예제 단순화를 위해 캐스팅함
        Book findBook = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(findBook.getId());
        form.setName(findBook.getName());
        form.setPrice(findBook.getPrice());
        form.setStockQuantity(findBook.getStockQuantity());
        form.setAuthor(findBook.getAuthor());
        form.setIsbn(findBook.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem (@ModelAttribute("form") BookForm form) {

        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }
}