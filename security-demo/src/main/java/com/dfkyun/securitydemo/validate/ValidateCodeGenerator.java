package com.dfkyun.securitydemo.validate;

import com.dfkyun.securitydemo.util.ImageCode;
import org.springframework.web.context.request.ServletWebRequest;
/**
 * 验证码生成器
 */
public interface ValidateCodeGenerator {
    /**
     * 创建验证码
     */
    ImageCode createCode(ServletWebRequest request);
}
