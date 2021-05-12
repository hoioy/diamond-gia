package com.hoioy.diamond.oauth2.support;

import com.hoioy.diamond.common.util.CommonRedisUtil;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import com.hoioy.diamond.oauth2.dto.OauthTokenDTO;
import com.hoioy.diamond.oauth2.service.IOauthTokenService;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.CollectionUtils;

import java.util.Date;

/**
 * redis缓存access_token和refresh_token
 */
public class CustomJwtTokenStore extends JwtTokenStore {
    private static Logger logger = LoggerFactory.getLogger(CustomJwtTokenStore.class);

    @Autowired
    public CommonRedisUtil redisUtil;

    @Autowired
    public IOauthTokenService oauthTokenService;

    /**
     * Create a JwtTokenStore with this token enhancer (should be shared with the DefaultTokenServices if used).
     *
     * @param jwtTokenEnhancer
     */
    public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        super.storeAccessToken(token, authentication);
        logger.debug("storeAccessToken");
        String key = GIAConfig.ACCESS_TOKEN_REDIS + ":" + token.getAdditionalInformation().get("sub") + ":" + token.getValue();
        token.getAdditionalInformation().put("redisKey", key);
        token.getAdditionalInformation().put("clientId", authentication.getOAuth2Request().getClientId());
        if (authentication.getUserAuthentication().getDetails() instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getUserAuthentication().getDetails();
            token.getAdditionalInformation().put("remoteAddress", webAuthenticationDetails.getRemoteAddress());
        }
        String value = JSONUtil.toJsonStr(token);

        OauthTokenDTO oauthToken = new OauthTokenDTO();
        oauthToken.setJti((String) token.getAdditionalInformation().get("jti"));
        oauthToken.setAccessToken(token.getValue());
        oauthToken.setSub((String) token.getAdditionalInformation().get("sub"));
        oauthToken.setExpiration(token.getExpiration().getTime());
        oauthToken.setExpiresIn(token.getExpiresIn());
        StringBuilder scopeBuilder = new StringBuilder();
        if (!CollectionUtils.isEmpty(token.getScope())) {
            token.getScope().forEach(s -> {
                scopeBuilder.append(s + ',');
            });
            scopeBuilder.deleteCharAt(scopeBuilder.lastIndexOf(","));
        }
        oauthToken.setScope(scopeBuilder.toString());
        oauthToken.setTokenType(token.getTokenType());
        oauthToken.setClientId(token.getAdditionalInformation().get("clientId").toString());
        if (token.getAdditionalInformation().get("remoteAddress") != null) {//密码模式没有remoteAddress
            oauthToken.setRemoteAddress(token.getAdditionalInformation().get("remoteAddress").toString());
        }

        if (token.getRefreshToken() != null) {
            //设置自动过期时间为refresh_token的过期时间
            DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken) token.getRefreshToken();
            oauthToken.setRefreshToken(refreshToken.getValue());
            oauthToken.setRefreshTokenExpiration(refreshToken.getExpiration().getTime());
        }

        //入库
        oauthTokenService.create(oauthToken);

        //缓存
        redisUtil.set(key, value);
        if (oauthToken.getRefreshTokenExpiration() != null) {
            redisUtil.expireAt(key, new Date(oauthToken.getRefreshTokenExpiration()));
        }

        logger.debug("storeAccessToken end key={},value={}", key, value);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        super.removeAccessToken(token);
        logger.debug("removeAccessToken");
        String key = GIAConfig.ACCESS_TOKEN_REDIS + ":" + token.getAdditionalInformation().get("sub") + ":" + token.getValue();
        //删除缓存
        redisUtil.remove(key);
        //删库
        oauthTokenService.removeById((String) token.getAdditionalInformation().get("jti"));

        logger.debug("removeAccessToken end key={}", key);
    }

//    @Override
//    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
//        super.storeRefreshToken(refreshToken, authentication);
//        logger.debug("storeRefreshToken");
//        refreshToken.
//        String key = RedisKey.REFRESH_TOKEN_REDIS + ":" + token.getAdditionalInformation().get("sub") + ":" + token.getValue();
//        token.getAdditionalInformation().put("redisKey",key);
//        String value = JSON.toJSONString(token);
//        redisUtil.set(key, value);
//        redisUtil.expireAt(key, token.getExpiration());
//        logger.debug("storeRefreshToken end key={},value={}", key, value);
//    }
//
//    @Override
//    public void removeRefreshToken(OAuth2RefreshToken token) {
//        super.removeRefreshToken(token);
//        logger.debug("removeRefreshToken");
//        String key = RedisKey.REFRESH_TOKEN_REDIS + ":" + token.getAdditionalInformation().get("sub") + ":" + token.getValue();
//        redisUtil.remove(key);
//        logger.debug("removeRefreshToken end key={}", key);
//    }
}
