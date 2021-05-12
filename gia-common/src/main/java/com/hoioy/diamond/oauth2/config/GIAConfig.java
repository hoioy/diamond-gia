package com.hoioy.diamond.oauth2.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Data
@NoArgsConstructor
public class GIAConfig {
    public static final String ACCESS_TOKEN_REDIS = "OAUTH2:access_token";
    public static final String REFRESH_TOKEN_REDIS = "OAUTH2:refresh_token";

    @Value("${diamond.default-authority-list}")
    private String[] defaultAuthorityList = new String[]{};

    //用户数据来源,除oauth2 server自身的User表，还支持三种类型扩展来源,默认为null
    @Value("${diamond.provider}")
    private ProviderType[] providerTypes = new ProviderType[]{};

    //LDAP相关参数配置
    @Value("${diamond.ldap.url}")
    private String URL;
    @Value("${diamond.ldap.admin-name}")
    private String adminName;
    @Value("${diamond.ldap.admin-password}")
    private String adminPassword;
    @Value("${diamond.ldap.base-dn}")
    private String BASEDN;

    @Value("${diamond.db.driverClassName}")
    private String driverClassName;
    @Value("${diamond.db.dbUrl}")
    private String dbUrl;
    @Value("${diamond.db.username}")
    private String username;
    @Value("${diamond.db.password}")
    private String password;
    @Value("${diamond.db.tableName}")
    private String tableName;
    @Value("${diamond.db.nameField}")
    private String nameField;
    @Value("${diamond.db.credentialField}")
    private String credentialField;

    @Value("${diamond.http.url}")
    private String httpUrl;
    @Value("${diamond.http.nameField}")
    private String httpNameField;
    @Value("${diamond.http.credentialField}")
    private String httpCredentialField;

    public final static String providerTypeAuthorityName = "ProviderType";

    public enum ProviderType {
        //diamond-gia的user_info表的用户数据源,默认包含
        SELF,
        //LDAP中数据
        LDAP,
        //直接对接外部数据库
        DB,
        //调用外部用户认证接口
        HTTP
    }
}
