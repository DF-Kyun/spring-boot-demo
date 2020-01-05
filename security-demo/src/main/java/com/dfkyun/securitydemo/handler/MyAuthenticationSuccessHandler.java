package com.dfkyun.securitydemo.handler;

import com.dfkyun.securitydemo.util.BrowserProperties;
import com.dfkyun.securitydemo.util.LoginType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrowserProperties browserProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("登录成功");

        //判断是json 格式返回 还是 view 格式返回
        if (LoginType.JSON.equals(browserProperties.getLoginType())){

            response.setContentType("application/json;charset=UTF-8");
            super.onAuthenticationSuccess(request,response,authentication);
        }else {
            //返回view
            super.onAuthenticationSuccess(request,response,authentication);
        }
    }
}
