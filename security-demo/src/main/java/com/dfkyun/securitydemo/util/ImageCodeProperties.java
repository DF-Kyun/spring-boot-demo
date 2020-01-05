package com.dfkyun.securitydemo.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
public class ImageCodeProperties {

    private int width;

    private int height;

    private int length;

    private int expireIn;
}
