package com.dfkyun.securitydemo.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 加载用户数据 , 返回UserDetail 实例
     * @param username  用户登录username
     * @return  返回User实体类 做用户校验
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户名:"+username);
        // TODO 需要数据库查找
        String password = passwordEncoder.encode("123456");
        //User三个参数   (用户名+密码+权限)
        //根据查找到的用户信息判断用户是否被冻结
        log.info("数据库密码:"+password);
        return new User(username,password,
                true,true,true,true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

    }
}
