package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 13/01/2020
 * Time: 7:59 오후
 **/
@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello (Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }
}
