package com.nowcoder.community.controller.advice;


import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Controller注解是用来对controller层做全局配置
 * 这里只扫描带controller的bean
 * 可以做三方面的全局配置
 * 异常数据处理
 * 绑定数据方案
 * 绑定参数方案
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * 在controller出现异常的时候该方法会被调用
     * @param e
     * @param request
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常："+e.getMessage());

        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            logger.error(stackTraceElement.toString());
        }


        /**
         * 判断是普通请求还是异步请求
         * 因为对于普通的请求 要求你返回的是页面 如果出现错误 那么返回500
         * 对于异步请求 浏览器希望你返回一个json 因此需要对异步和普通请求分开处理。
         */

        String header = request.getHeader("x-requested-with");

        if("XMLHttpRequest".equals(header)){
            //如果是异步请求，响应json字符串
            response.setContentType("application/plain;charset=utf-8");

            PrintWriter writer = response.getWriter();

        writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
        }else{
            response.sendRedirect(request.getContextPath()+"/error");
        }


    }
}
