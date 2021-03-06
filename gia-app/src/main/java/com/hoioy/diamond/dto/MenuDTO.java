package com.hoioy.diamond.dto;

import com.hoioy.diamond.common.dto.BaseDTO;
import com.hoioy.diamond.domain.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 类名称：MenuDto
 * 类描述：   菜单DTO
 * 创建人：dourl
 * 创建时间：2018年2月5日 下午2:06:50
 */
@Data
public class MenuDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -2858011414460214454L;

    private String menuDesc;

    private Integer menuIndex;

    private String menuName;

    private String menuUrl;

    private String parentId;

    private String parentName;

    private Integer flag;

    private String mark;

    private List<MenuDTO> children;

    private Set<Role> roles;
}