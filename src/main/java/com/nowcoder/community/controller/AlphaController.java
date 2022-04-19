package com.nowcoder.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@Controller
public class AlphaController {

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        //获取请求数据
        System.out.println("request.getContextPath(): "+request.getContextPath());
        System.out.println("request.getClass()  "+request.getClass());

        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);

            System.out.println(name + "  " + value);
        }

        System.out.println(request.getParameter("code"));


        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try{
            PrintWriter writer = response.getWriter();
            writer.write("<h1>牛客</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
