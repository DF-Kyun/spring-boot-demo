/**
 * 
 */
package com.dfkyun.microweather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * REST配置.
 * 使用 Apache HttpClient 作为 REST 客户端。Apache HttpClient 内置了对于 GZIP 的支持
 */
@Configuration
public class RestConfiguration {

    @Bean  
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate(
                new HttpComponentsClientHttpRequestFactory()); // 使用HttpClient，支持GZIP
        restTemplate.getMessageConverters().set(1,
                new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
        return restTemplate;
    }

}
