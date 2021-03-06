package com.dfkyun.securitydemo.handler;

import com.dfkyun.securitydemo.util.BrowserProperties;
import com.dfkyun.securitydemo.util.LoginType;
import com.dfkyun.securitydemo.util.SimpleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrowserProperties browserProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        logger.info("登录失败");

        //如果是json 格式
        if (LoginType.JSON.equals(browserProperties.getLoginType())){
            //设置状态码
            response.setStatus(500);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(e.getMessage())));
        }else{
            //如果不是json格式，返回view
            super.onAuthenticationFailure(request,response,e);
        }
    }
}
