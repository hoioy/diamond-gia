package com.hoioy.diamond.oauth2.support;

import com.hoioy.diamond.common.util.CommonRedisUtil;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.util.CollectionUtils;

import java.util.List;

//自定义CustomTokenServices ， 解决刷新token问题https://github.com/spring-projects/spring-security-oauth/issues/685
public class CustomTokenServices extends DefaultTokenServices {
    private TokenStore tokenStore;

    private CommonRedisUtil redisUtil;

    //同一用户最多允许同时登录个数
    private Integer maximumConcurrentUser;

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        String key = GIAConfig.ACCESS_TOKEN_REDIS + ":" + authentication.getName() + ":*";
        List<String> tokens = redisUtil.mgetByPattern(key);
        if (CollectionUtil.isNotEmpty(tokens) && tokens.size() >= maximumConcurrentUser) {
            throw new SessionAuthenticationException("超过同一用户最大并发数：" + maximumConcurrentUser);
        }
        return super.createAccessToken(authentication);
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
        OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(refreshTokenValue);
        OAuth2Authentication authentication = this.tokenStore.readAuthenticationForRefreshToken(refreshToken);

        // Check attributes in the authentication and
        // decide whether to grant the refresh token
        boolean allowRefresh = false;

        List<String> tokens = redisUtil.mgetByPattern(GIAConfig.ACCESS_TOKEN_REDIS + ":"
                + authentication.getName() + ":*");
        if (!CollectionUtils.isEmpty(tokens)) {
            for (String token : tokens) {
                JSONObject jsonObject = JSONUtil.parseObj(token);

                if (jsonObject.get("refreshToken") != null) {
                    JSONObject rtJson = (JSONObject) jsonObject.get("refreshToken");
                    String rt = (String) rtJson.get("value");
                    if (rt.equals(refreshToken.getValue())) {
                        //存在一样的token，说明用户没有主动删除token，可以继续进行刷新操作
                        allowRefresh = true;
                        break;
                    }
                }
            }
        }

        if (!allowRefresh) {
            // throw UnauthorizedClientException or something similar
            throw new InvalidGrantException("refresh token已失效，被管理员主动删除" + refreshTokenValue);
        }
        return super.refreshAccessToken(refreshTokenValue, tokenRequest);
    }


    public void setMaximumConcurrentUser(Integer maximumConcurrentUser) {
        this.maximumConcurrentUser = maximumConcurrentUser;
    }

    public void setRedisUtil(CommonRedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void setTokenStore(TokenStore tokenStore) {
        super.setTokenStore(tokenStore);
        this.tokenStore = tokenStore;
    }
}
