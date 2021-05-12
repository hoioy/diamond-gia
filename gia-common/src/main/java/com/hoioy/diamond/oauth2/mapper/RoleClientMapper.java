package com.hoioy.diamond.oauth2.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hoioy.diamond.common.base.IBaseJoinMapper;
import com.hoioy.diamond.oauth2.domain.RoleClient;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsWithRoleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface RoleClientMapper extends IBaseJoinMapper<RoleClient> {

    IPage<RoleClient> selectPage(@Param("page") Page page, @Param("roleClient") RoleClient roleClient);

    IPage<Map> findOauthClientDetailsWithRole(@Param("page") Page page, @Param("oauthClientDetailsWithRoleDTO") OauthClientDetailsWithRoleDTO oauthClientDetailsWithRoleDTO);
}
