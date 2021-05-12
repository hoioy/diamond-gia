## 简介
* TDF-oauth-server，Diamond GIA统一认证平台。TDF-oauth-server采用spring boot框架结构，是在spring security和spring security oauth2基础上构建的
Oauth2的认证服务器(Authentication Server)，同时也是资源服务器（Resource Server)。可以独立部署TDF-oauth-server来统一管理用户的认证和授权，
权限管理目前支持对用户所属的角色授予第三方应用(client)的可见权限，不控制每个第三方应用（client）内部的权限。也可以将第三方应用（client）注册到
太极内网线上运行的[Diamond GIA统一认证平台](http://gia.diamond.hoioy.com)。
* TDF-oauth-client是TDF-oauth-server项目的客户端演示代码，作为Oauth2的第三方应用（client）演示单点登录等功能。
* [太极开发者社区](http://gitlab.hoioy.com/IRI/community)使用了TDF-oauth-server做第三方认证服务器

## 资料
太极开发者社区博客[统一认证平台TDF-oauth-server](http://tech.hoioy.com/#/blog/view/7eb7cba83b6b4dc38216dda46057e4e8)

### 源码分支说明
1. master 最新稳定分支 基于spring boot 2.x
2. dev 开发主分支

## 环境
* JDK1.8
* Mysql5.6+ 注意mysql版本

## 运行
### 源码构建
* 如果找不到TDF-bom可以访问TDF-bom项目地址：https://gitlab.hoioy.com/IRI/TDF-Base/tdf-base-platform 相关版本分支进行本地构建

### 基础功能运行
* 准备数据库,执行tdf_oauth2.ddl和tdf_oauth2.sql
* 修改yml中数据库相关配置
* 在IDE中直接运行TdfOauth2ServerV2Application（或控制台项目根路径下运行：mvn spring-boot:run -e）
* 浏览器输入：http://localhost:8080
* 超级管理员用户/密码：admin/123456, 普通用户/密码：user/123456，其他LDAP/DB/HTTP用户来源参考配置文件

### 演示第三方应用认证功能
* 下载[TDF-oauth-client](https://gitlab.hoioy.com/IRI/tdf-auth/tdf-oauth-client)
* 运行TDF-oauth-client演示登录退出等功能

### 演示支持多种用户数据来源
TDF-oauth-server支持了4种用户来源：
1. 来源于TDF-oauth-server本地数据库的user_info表
2. 来源于LDAP服务
3. 来源于外接数据库
4. 来源于HTTP接口

* 配置yml配置文件中tdf.provider的配置。例如需要使用LDAP数据源中的用户信息，就配置：tdf.provider=LDAP和tdf.ldap相关属性
参考:com.hoioy.diamond.config.SysConfig.ProviderType:
```java
 public enum ProviderType {
        //tdf-oauth-server的user_info表的用户数据源,默认包含
        SELF,
        //太极LDAP中数据
        LDAP,
        //直接对接外部数据库
        DB,
        //调用外部用户认证接口
        HTTP
    }
```
和com.hoioy.diamond.config.SecurityConfig的方法：
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //执行顺序：Dao-》ldap-》db-》http
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

    if (ArrayUtils.contains(sysConfig.getProviderTypes(), SysConfig.ProviderType.LDAP)) {
        auth.authenticationProvider(customLdapAuthenticationProvider());
    }

    if (ArrayUtils.contains(sysConfig.getProviderTypes(), SysConfig.ProviderType.DB)) {
        auth.authenticationProvider(dbAuthenticationProvider());
    }

    if (ArrayUtils.contains(sysConfig.getProviderTypes(), SysConfig.ProviderType.HTTP)) {
        auth.authenticationProvider(httpAuthenticationProvider());
    }
}
```

### 开发调试显示Log
* 在yml配置文件中security包下的log级别设置为：debug
```yml
logging:
  level:
    org:
      springframework:
        security: debug
```

* 修改com.hoioy.diamond.config.SecurityConfig的@EnableWebSecurity(debug = true):
```java
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
```

## 关键配置说明
1. pom引入spring-security-security,spring-security-oauth2相关包
2. com.hoioy.diamond.config.AuthorizationServerConfig配置认证服务器
3. com.hoioy.diamond.config.ResourceServerConfig配置资源服务器
4. com.hoioy.diamond.config.SecurityConfig为spring security的基础安全配置文件

### 认证授权与单点登录配置：
* 认证服务器：支持四种token存储方式，同时只能开启其中一种：AuthorizationServerConfig（内存），AuthorizationServerJDBCTokenStoreConfig，AuthorizationServerJWTTokenStoreConfig，AuthorizationServerRedisTokenStoreConfig
* 资源服务器：@EnableResourceServer注解需要注解在ResourceServerConfigurerAdapter子类上

### 单点退出
关于登出：
server登出 -》  所有客户端不变
client1登出  -》server登出  client2状态不变

关于登入：
server或者client任何一个登入  -》sever何全部client登入

### 支持JWTs
AuthorizationServerJWTTokenStoreConfig的JWT格式token的支持JWK签名

### 其他web操作功能
支持用户管理、角色管理、应用管理、角色相关权限管理等等，具体详情请参考实际运行页面。

#### 新建应用
1. 新建应用时的各个字段说明请参考（需要登录公司内网gitlab）：http://gitlab.hoioy.com/IRI/tdf/TDF-oauth-server/wikis/%E6%96%B0%E5%BB%BA%E5%BA%94%E7%94%A8-%E8%BE%93%E5%85%A5%E5%AD%97%E6%AE%B5%E8%AF%B4%E6%98%8E

### 部署
[部署](./doc/部署.md)

### 关于ResourceServer
[其他统一认证服务器是否支持Resource Server机制调研](./doc/问题与解决方案.md)

### 关于基于Spring boot2.x的oauth2相关详细配置说明可参考:
* [spring-security-oauth-sample](http://gitlab.hoioy.com/zhaozhao/spring-security-oauth-sample)
* [spring-security-sample](http://gitlab.hoioy.com/zhaozhao/spring-security-sample)

### doc中的文档导出为PDF
1. 用户手册.md命名为readme.md
1. gitbook pdf . 用户手册.pdf
