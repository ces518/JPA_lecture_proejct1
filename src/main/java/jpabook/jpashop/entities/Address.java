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
}
