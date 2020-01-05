package com.dfkyun.oauth2demo.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "my.security")
public class BrowserProperties {

    private String loginPage;

    private LoginType loginType;
}
