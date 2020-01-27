package jpabook.jpashop.entities.item;

import jpabook.jpashop.entities.Category;
import jpabook.jpashop.exception.NotEnoughException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 14/01/2020
 * Time: 11:22 오후
 **/
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") // 구분값 컬럼 기본값은 DTYPE
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 도메인주도 설계시
     * 엔티티가 해결할 수 있는 비즈니스 로직은 엔티티가 가지고 있는것이 좋다.
     * 데이터가 가지고 있는쪽에서 비즈니스 로직을 두는것이 좋음
     * 객체지향적인 설계
     * 응집도 증가
     */

    /**
     * 재고 증가
     * @param quantity
     */
    public void addStock (int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     * @param quantity
     */
    public void removeStock (int quantity) {
        int resultStock = this.stockQuantity - quantity;
        if (resultStock < 0) {
            throw new NotEnoughException("need more stock");
        }
        this.stockQuantity = resultStock;
    }
}
