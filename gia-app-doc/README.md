## 简介
Diamond GIA是一个标准的OAuth2授权中心，简洁精巧、快速部署的设计理念。
采用Spring Boot框架，主要在spring security、spring security oauth2、diamond基础上构建。

Diamond GIA同时也是资源服务器（Resource Server)。

Diamond GIA可以独立为鉴权中心、统一管理用户、统一认证等与其他设施集成。

## 技术支持
* [![Gitter](https://badges.gitter.im/hoioy-diamond/community.svg)](https://gitter.im/hoioy-diamond/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
* [在线文档](http://doc.gia.diamond.hoioy.com)

## 工程地址
* Github： https://github.com/hoioy/diamond-gia.git
* 码云：https://gitee.com/hoioy/diamond-gia.git

## 演示环境
* [Diamond GIA](http://gia.diamond.hoioy.com) 
    * 用户名/密码：admin/123456
* [演示其他应用使用GIA登录](http://diamond.hoioy.com)
    * 点击登录页黄色按钮"Diamond统一认证登录"进行登录

## 环境依赖
#### 必选
- jdk 1.8 +
- maven 3+
- lombok IDE插件

#### 可选
- mysql 8
- redis

## 运行
* 方式一:
    1. 下载jar运行：https://gitee.com/hoioy/diamond-gia/releases/v1.0.1  
    1. java -jar gia-app-1.0.1.jar
* 方式二:
    1. 下载源码，maven构建完成
    1. 直接运行启动 gia-app
1. 默认管理员用户名/密码：admin/123456
1. 访问后端h2数据库http://{ip}:{port}/h2-console
    * 数据库路径：~/db/diamondgia
    * 用户名/密码：sa/sa
    
> 默认是使用caffeine缓存，没有配置Redis，因此可能报错日志：
>   ```
>    o.s.d.r.l.RedisMessageListenerContainer  : Connection failure occurred. Restarting subscription task after 5000 ms
>   ```
>   这个不影响运行。
  
## 项目构建
* GIA是maven工程。
* 默认使用h2内存数据库和内存 caffeine 缓存，便于快速运行。
* 生产环境建议切换为Mysql和Redis。

### 编译
`mvn compile`

### 打包
`mvn package -Dmaven.test.skip=true`

### 安装到本地仓库
`mvn install -Dmaven.test.skip=true`


## 贡献代码

欢迎您贡献代码，发起pull request。也可以联系我们将您添加进入开发组。

## 版权信息
版权归www.hoioy.com所有