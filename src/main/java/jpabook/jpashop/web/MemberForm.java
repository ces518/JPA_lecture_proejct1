package jpabook.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-05
 * Time: 18:16
 **/
@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원의 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
