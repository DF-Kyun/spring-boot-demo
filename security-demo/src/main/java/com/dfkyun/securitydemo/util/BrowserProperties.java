package com.dfkyun.securitydemo.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "my.security")
public class BrowserProperties {

    private String loginPage;

    private LoginType loginType;

    private ImageCodeProperties image = new ImageCodeProperties();
}
