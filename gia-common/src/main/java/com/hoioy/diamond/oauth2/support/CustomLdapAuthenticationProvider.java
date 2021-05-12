package com.hoioy.diamond.oauth2.support;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.hoioy.diamond.log.service.IWebLogsService;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by andyzhaozhao on 2018/8/21.
 */
@Component
public class CustomLdapAuthenticationProvider implements AuthenticationProvider, ICustomProvider {
    private static final Logger log = LoggerFactory.getLogger(HttpAuthenticationProvider.class);

    @Autowired
    private UserAuthenticate userAuthenticate;

    @Autowired
    private IWebLogsService iWebLogsService;

    @Autowired
    private GIAConfig giaConfig;

    private String email = "@qq.com";


    //TODO 缺少跟本地用户表匹对代码
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        if (StrUtil.isEmpty(username)) {
            throw new AuthenticationServiceException("User name can't be null");
        }

        if (StrUtil.isEmpty(password)) {
            throw new AuthenticationServiceException("password can't be null");
        }

        username = username.trim();
        boolean isHad = username.indexOf("@") > 0;
        if (!isHad) {
            username = username + email;
        }
        Map map = userAuthenticate.authenricateName(username, password);
        if (map.isEmpty()) {
            throw new AuthenticationServiceException("登录名称或密码不正确，请重新输入");
        }

        iWebLogsService.create(getLoginLogDTO(username, GIAConfig.ProviderType.LDAP));

        //TODO 获得用户的权限上下文，ldap用户只有访问portal的权限
        List<String> as = ListUtil.toList(giaConfig.getDefaultAuthorityList());
        as.add(GIAConfig.providerTypeAuthorityName + GIAConfig.ProviderType.LDAP);
        List<SimpleGrantedAuthority> authorities = initAuthorities(as);

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(), authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
