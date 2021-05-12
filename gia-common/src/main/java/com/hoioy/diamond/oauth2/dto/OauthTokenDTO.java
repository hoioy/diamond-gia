package com.hoioy.diamond.oauth2.dto;

import com.hoioy.diamond.common.dto.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OauthTokenDTO extends BaseDTO {
    private String jti;

    private String accessToken;
    /**
     * 用户名唯一
     */
    private String sub;

    private Long expiration;
    /**
     * 还有多少秒过期
     */
    private Integer expiresIn;
    /**
     * 权限范围，多个逗号分开
     */
    private String scope;
    /**
     * token类型，如bearer
     */
    private String tokenType;
    /**
     * token所属的客户端id
     */
    private String clientId;

    private String remoteAddress;

    //刷新token
    private String refreshToken;

    // 刷新token过期时间
    private Long refreshTokenExpiration;
}
