package com.hoioy.diamond.oauth2.dto;

import com.hoioy.diamond.common.dto.CommonUserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 第三方平台对接使用的最简用户结构
 */
@Data
@NoArgsConstructor
public class OAuth2UserDTO extends CommonUserDTO implements Serializable {
    private static final long serialVersionUID = 1170018455276020807L;

    private String email;
}