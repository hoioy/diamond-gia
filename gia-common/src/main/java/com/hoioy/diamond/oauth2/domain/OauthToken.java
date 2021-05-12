package com.hoioy.diamond.oauth2.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.hoioy.diamond.common.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("oauth_token")
public class OauthToken extends BaseDomain {
    private static final long serialVersionUID = 1724450140216701196L;

    @TableField("jti")
    private String jti;

    @TableField("access_token")
    private String accessToken;
    /**
     * 用户名唯一
     */
    @TableField("sub")
    private String sub;

    @TableField("expiration")
    private Long expiration;
    /**
     * 还有多少秒过期
     */
    @TableField("expires_in")
    private Integer expiresIn;
    /**
     * 权限范围，多个逗号分开
     */
    @TableField("scope")
    private String scope;
    /**
     * token类型，如bearer
     */
    @TableField("token_type")
    private String tokenType;
    /**
     * token所属的客户端id
     */
    @TableField("client_id")
    private String clientId;

    @TableField("remote_address")
    private String remoteAddress;

    //刷新token
    @TableField("refresh_token")
    private String refreshToken;

    // 刷新token过期时间
    @TableField("refresh_token_expiration")
    private Long refreshTokenExpiration;
}
