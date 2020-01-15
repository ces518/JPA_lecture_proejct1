package jpabook.jpashop.entities;

import jpabook.jpashop.entities.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 15/01/2020
 * Time: 10:54 오후
 **/
@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    // 객체는 다대다 컬렉션 참조가 가능하지만, 관계형 DB에서는 불가능하다.
    // 중간 테이블로 매핑을 해주어야한다.
    // 객체와 관계형 DB의 차이점을 해소하기 위함
    // 실무에서는 다대다 매핑은 사용하지 말것.
    // 컬럼 추가등 커스터마이징이 불가능하다.
    @ManyToMany
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id") // 반대편인 아이템에 해당하는 FK 컬럼명 설정
    )
    private List<Item> items = new ArrayList<>();

    // 계층형 구조의 부모 매핑
    // 다대일 양방향 매핑
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 계층형 구조의 자식 매핑
    // 일대다 양방향 매핑
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();
}
