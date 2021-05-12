package com.hoioy.diamond.oauth2.service;

import com.hoioy.diamond.common.domain.CommonDomain;
import com.hoioy.diamond.common.dto.PageDTO;
import com.hoioy.diamond.common.service.IBaseJoinService;
import com.hoioy.diamond.oauth2.domain.RoleClient;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsWithRoleDTO;
import com.hoioy.diamond.oauth2.dto.RoleClientJoinDTO;

import java.util.Set;

public interface IRoleClientService<D extends CommonDomain> extends IBaseJoinService<RoleClientJoinDTO, RoleClient> {
    void batchDeleteRoleClientByClientIds(Set<String> clientIds);

    /**
     * JPA多表关联动态分页查询示例
     */
    PageDTO<OauthClientDetailsWithRoleDTO> findOauthClientDetailsWithRole(final PageDTO<OauthClientDetailsWithRoleDTO> pageDTO);
}