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
@TableName("oauth_client_details")
public class OauthClientDetails extends BaseDomain {
    private static final long serialVersionUID = 1724450140216701197L;

    /**
     * 客户端id
     */
    @TableField("client_id")
    private String clientId;
    /**
     * 资源
     */
    @TableField("resource_ids")
    private String resourceIds;
    /**
     * 客户隐私
     */
    @TableField("client_secret")
    private String clientSecret;
    /**
     * 范围
     */
    @TableField("scope")
    private String scope;
    /**
     * 授权类型
     */
    @TableField("authorized_grant_types")
    private String authorizedGrantTypes;
    /**
     * 服务器重定向URI
     */
    @TableField("web_server_redirect_uri")
    private String webServerRedirectUri;
    /**
     * 权限
     */
    @TableField("authorities")
    private String authorities;
    /**
     * 访问令牌的有效性
     */
    @TableField("access_token_validity")
    private Integer accessTokenValidity;
    /**
     * 刷新令牌的有效性
     */
    @TableField("refresh_token_validity")
    private Integer refreshTokenValidity;
    /**
     * 额外信息
     */
    @TableField("additional_information")
    private String additionalInformation;
    /**
     * 自动批准(实际显示的是 应用名称)
     */
    @TableField("autoapprove")
    private String autoapprove;

    @TableField("custom_name")
    private String customName;
}
