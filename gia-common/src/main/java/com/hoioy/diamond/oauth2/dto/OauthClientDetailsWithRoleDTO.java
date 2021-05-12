package com.hoioy.diamond.oauth2.dto;

import com.hoioy.diamond.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关联查询DTO
 */
@Data
@NoArgsConstructor
public class OauthClientDetailsWithRoleDTO extends BaseDTO {
    private static final long serialVersionUID = 25333699L;

    @ApiModelProperty(value = "应用ID", required = true, position = 0)
    private String clientId;
    @ApiModelProperty(value = "应用密钥", required = true, position = 1)
    private String clientSecret;
    @ApiModelProperty(value = "授权模式。该Client支持的授权模式，OAuth2的Client请求code时会传递授权模式参数，该处包含的授权模式才可以访问", required = true, position = 2,
            example = "authorization_code, implicit")
    private String authorizedGrantTypes;
    @ApiModelProperty(value = "该Client分配的access_token的有效时间，要小于刷新时间", required = true, position = 3, example = "7200")
    private Integer accessTokenValidity;
    @ApiModelProperty(value = "该Client分配的access_token的可刷新时间，要大于有效时间。超过有效时间，但是在可刷新时间范围的access_token可以刷新"
            , required = true, position = 4, example = "72000")
    private Integer refreshTokenValidity;
    @ApiModelProperty(value = "服务器重定向URL。多个url使用逗号间隔", required = true, position = 5)
    private String webServerRedirectUri;
    @ApiModelProperty(value = "OAuth2客户端申请的权限范围", required = true, position = 6, example = "read,write,profile")
    private String scope;
    @ApiModelProperty(value = "用户是否自动批准（Approval）， 默认值为 “false”，可选值为scope属性中定义的值", required = true, position = 7, example = "read,write")
    private String autoapprove;
    @ApiModelProperty(value = "可访问的资源服务器id，多个资源id使用逗号隔开", position = 8, hidden = true)
    private String resourceIds;
    @ApiModelProperty(value = "OAuth2客户端拥有的Spring Security的权限值", position = 9, hidden = true)
    private String authorities;
    @ApiModelProperty(value = "预留的字段。注册OAuth2客户端时由客户端填写，可以为null。该属性值必须是JSON格式的字符串"
            , position = 10, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String additionalInformation;
    @ApiModelProperty(value = "自定义字段：应用名称", required = true, position = 11)
    private String customName;
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    public OauthClientDetailsWithRoleDTO(
            String id,
            String clientId,
            String clientSecret,
            String authorizedGrantTypes,
            Integer accessTokenValidity,
            Integer refreshTokenValidity,
            String webServerRedirectUri,
            String scope,
            String autoapprove,
            String resourceIds,
            String authorities,
            String customName,
            String roleName,
            String additionalInformation) {
        super.setId(id);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.webServerRedirectUri = webServerRedirectUri;
        this.scope = scope;
        this.autoapprove = autoapprove;
        this.resourceIds = resourceIds;
        this.authorities = authorities;
        this.customName = customName;
        this.roleName = roleName;
        this.additionalInformation = additionalInformation;
    }
}