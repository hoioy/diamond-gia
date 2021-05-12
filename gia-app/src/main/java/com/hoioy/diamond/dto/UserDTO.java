package com.hoioy.diamond.dto;

import com.hoioy.diamond.oauth2.dto.OAuth2UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.Set;

/**
 * 类名称：UserDTO 类描述： 用户DTO 创建人：dourl 创建时间：2018年2月5日 下午2:07:16
 */
@Data
@NoArgsConstructor
public class UserDTO extends OAuth2UserDTO implements Serializable {
    private static final long serialVersionUID = 1170018455276020707L;

    private Integer userIndex;
    private String state;
    private String userName;
    private String phoneNum;
    private Integer flag;

    // 昵称
    private String nickname;
    // 性别
    private String gender;
    // 居住地
    private String address;
    // 博客
    private String blog;
    // 标签
    private String tag;
    // 头像路径
    private String avatar;
    // 身份证号
    private String idNumber;
    // 出生日期
    private String birthday;
    // 用户积分
    private Integer integral;

    private String file;

    private Set<RoleDTO> roles;

//    @Override
//    protected String getSerialVersionUID() {
//        return Long.toString(serialVersionUID);
//    }

    public void generateTokenForCommunity(String salt) {
        if (salt == null)
            setToken(DigestUtils.sha1Hex(StrUtil.trimToEmpty(userName) +
                    StrUtil.trimToEmpty(getEmail()) + StrUtil.trimToEmpty(getId()) + Long.toString(serialVersionUID)));
        else
            setToken(DigestUtils.sha1Hex(StrUtil.trimToEmpty(userName) +
                    StrUtil.trimToEmpty(getEmail()) + StrUtil.trimToEmpty(getId()) + salt));

    }

    public boolean tokenKeepedForCommunity(String salt) {
        if (salt == null)
            return (DigestUtils.sha1Hex(StrUtil.trimToEmpty(userName) +
                    StrUtil.trimToEmpty(getEmail()) + StrUtil.trimToEmpty(getId()) + Long.toString(serialVersionUID))).equals(getToken());
        else
            return DigestUtils.sha1Hex(StrUtil.trimToEmpty(userName) +
                    StrUtil.trimToEmpty(getEmail()) + StrUtil.trimToEmpty(getId()) + salt).equals(getToken());

    }
}