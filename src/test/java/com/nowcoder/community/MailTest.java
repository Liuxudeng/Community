package com.nowcoder.community;

import com.nowcoder.CommunityApplication;
import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest


public class MailTest {
    @Autowired
    MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void  mailtoother(){

       // MailTest mt = new MailTest();
        mailClient.sendMail("1833746919@qq.com","Test","welcome");
    }


    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "sunday");

        String content = templateEngine.process("/mail/demo", context);
     //   System.out.println(content);

        mailClient.sendMail("1833746919@qq.com", "HTML", content);
    }
}
