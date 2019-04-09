# spring-boot-demo
关于Spring Boot的一些demo练习

## springboot-elasticsearch  
通过封装es的Java api对外提供rest服务  

## micro-weather
一个简单的天气服务，通过包装一个公网天气服务地址，提供查询能力  
1、通过解析拼装，提供基本的查询能力       
2、添加redis缓存，缓存服务访问内容，提升并发能力     
3、使用thymeleaf构建简单的展示页面，并设置热部署相关   

## kettle-server
通过封装kettle API，提供服务，创建kettle转换及作业  
1、具体测试参数类