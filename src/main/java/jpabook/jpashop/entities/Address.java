package jpabook.jpashop.entities;

import lombok.Getter;

import javax.persistence.Embeddable;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 14/01/2020
 * Time: 11:02 오후
 **/
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 에서는 기본생성자가 반드시 필요하다.
    // public 생성자가 존재한다면 불변객체로 생성하기가 힘들 경우가많다.
    // 따라서 JPA 에서는 protected 생성자까지 허용한다.
    protected Address() {
    }

    public Address (String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
