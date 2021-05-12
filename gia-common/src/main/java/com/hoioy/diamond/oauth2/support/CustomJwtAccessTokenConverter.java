package com.hoioy.diamond.oauth2.support;

import com.hoioy.diamond.oauth2.service.IRoleClientService;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Collection;
import java.util.List;

/**
 * 2.3.5版本JWT token没有sub字段，不符合标准，自定义补充sub字段
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
//        result.getAdditionalInformation().put("sub", authentication.getName());
//        return super.enhance(result, authentication);
//    }

    @Autowired
    private IRoleClientService roleClientService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        // 获取token的接口增加自定义过滤器，只有拥有此第三方应用（Client）权限的用户才可以授权Client获取token
        String clientId = authentication.getOAuth2Request().getClientId();
        Collection<GrantedAuthority> roles = authentication.getAuthorities();

        List<String> roleNames = roleClientService.findSecondIdsByFirstId(clientId);

        //默认白名单策略：如过client没有分配任何角色，则任何角色都可以访问
        //一旦分配了角色，则只有此角色可访问
        boolean hasAuthority = false;
        if (CollectionUtil.isEmpty(roleNames)) {
            hasAuthority = true;
        } else {
            for (String roleName : roleNames) {
                if (hasAuthority) {
                    break;
                }
                for (GrantedAuthority role : roles) {
                    if (roleName.equals(role.getAuthority())) {
                        hasAuthority = true;
                        break;
                    }
                }
            }
        }

        if (hasAuthority) {
            DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
            result.getAdditionalInformation().put("sub", authentication.getName());
            return super.enhance(result, authentication);
        }

        throw new AccessDeniedException("此用户没有访问此应用的权限");
    }
}
