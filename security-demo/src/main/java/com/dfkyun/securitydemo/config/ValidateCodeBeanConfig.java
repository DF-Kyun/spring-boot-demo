package com.dfkyun.securitydemo.config;

import com.dfkyun.securitydemo.util.BrowserProperties;
import com.dfkyun.securitydemo.util.ImageCodeProperties;
import com.dfkyun.securitydemo.validate.ImageCodeGenerator;
import com.dfkyun.securitydemo.validate.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private BrowserProperties browserProperties;

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setBrowserProperties(browserProperties);
        return codeGenerator;
    }
}
