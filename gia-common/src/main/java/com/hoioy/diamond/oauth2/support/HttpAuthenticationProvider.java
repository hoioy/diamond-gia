package com.hoioy.diamond.oauth2.support;

import com.hoioy.diamond.log.service.IWebLogsService;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import com.hoioy.diamond.oauth2.dto.OAuth2UserDTO;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
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
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * http远程调用用户认证接口进行认证
 */
@Component
public class HttpAuthenticationProvider implements AuthenticationProvider, ICustomProvider {
    private static final Logger log = LoggerFactory.getLogger(HttpAuthenticationProvider.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GIAConfig giaConfig;

    @Autowired
    private IWebLogsService iWebLogsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if (StrUtil.isEmpty(username)) {
            throw new AuthenticationServiceException("User name can't be null");
        }

        String url = giaConfig.getHttpUrl() + "?" + giaConfig.getHttpNameField() + "=" + username + "&"
                + giaConfig.getHttpCredentialField() + "=" + password;
        //get
        OAuth2UserDTO result = restTemplate.getForEntity(url, OAuth2UserDTO.class).getBody();

        if (result == null) {
            throw new AuthenticationServiceException("登录名称或密码不正确，请重新输入");
        }

        iWebLogsService.create(getLoginLogDTO(username, GIAConfig.ProviderType.HTTP));

        //TODO 获得用户的权限上下文，ldap用户只有访问portal的权限
        List<String> as = ListUtil.toList(giaConfig.getDefaultAuthorityList());
        as.add(GIAConfig.providerTypeAuthorityName + GIAConfig.ProviderType.HTTP);
        List<SimpleGrantedAuthority> authorities = initAuthorities(as);

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(), authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    public Map<String, String> getUser(String username) {
        String url = giaConfig.getHttpUrl() + "?" + giaConfig.getHttpNameField() + "=" + username;
        //get
        Map<String, String> result = restTemplate.getForEntity(url, Map.class).getBody();
        Map<String, String> newMap = new LinkedHashMap<>();

        newMap.put("name", username);
        newMap.put("email", newMap.get("email"));
        return result;
    }

}