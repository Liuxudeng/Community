package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@Controller
//@RequestMapping("/hello")
public class Hello {
    /**
     * @requestmapping注解就是请求映射到这个地址
     * @ResponseBody是返回的消息体，也就是返回的方法
     * 注意，这两个都是加在方法上，而在类上要加的注解是@controller，controller注解是为了能够被扫描到
     * 如果类中有多个方法，那么会在类上也加上一个@requestMapping地址，使得方法的RequestMapping为二级地址
     * @return
     */
    @RequestMapping("/hello1")
    @ResponseBody
    public String hello(){
        return "hello0416";
    }
}
