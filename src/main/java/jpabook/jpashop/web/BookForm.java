package jpabook.jpashop.web;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-06
 * Time: 22:33
 **/
@Getter @Setter
public class BookForm {

    private Long id;

    // == 상품 공통 속성 == //
    private String name;
    private int price;
    private int stockQuantity;
    // == == //

    private String author;
    private String isbn;
}
