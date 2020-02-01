package jpabook.jpashop.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-01
 * Time: 23:46
 **/
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    // 롬복으로 대체
//    Logger loger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public String home () {
        log.info("home controller .. ");
        return "home";
    }
}
