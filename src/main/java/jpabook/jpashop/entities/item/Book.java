package jpabook.jpashop.entities.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 14/01/2020
 * Time: 11:26 오후
 **/
@Entity
@DiscriminatorValue("B") // 구분값 벨류 설정
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;
}
