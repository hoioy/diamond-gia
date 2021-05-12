package com.hoioy.diamond.oauth2.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hoioy.diamond.common.annotation.BaseJoinId;
import com.hoioy.diamond.common.base.BaseJoinServiceImpl;
import com.hoioy.diamond.common.dto.PageDTO;
import com.hoioy.diamond.common.util.CommonMybatisPageUtil;
import com.hoioy.diamond.oauth2.domain.RoleClient;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsWithRoleDTO;
import com.hoioy.diamond.oauth2.dto.RoleClientJoinDTO;
import com.hoioy.diamond.oauth2.mapper.RoleClientMapper;
import com.hoioy.diamond.oauth2.service.IRoleClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoleClientServiceImpl extends BaseJoinServiceImpl<RoleClientMapper, RoleClient, RoleClientJoinDTO>
        implements IRoleClientService<RoleClient> {
    private static final Logger logger = LoggerFactory.getLogger(RoleClientServiceImpl.class);

    @Transactional
    @Override
    public void batchDeleteRoleClientByClientIds(Set<String> clientIds) {
        logger.info("batchDeleteRoleClientByClientIds");
        List<String> ids = findIdsByJoinIds(new ArrayList<>(clientIds), BaseJoinId.Index.first);
        int count = iBaseRepository.deleteBatchIds(ids);
        logger.debug("批量删除:" + count);
    }

    @Override
    public PageDTO<OauthClientDetailsWithRoleDTO> findOauthClientDetailsWithRole(PageDTO<OauthClientDetailsWithRoleDTO> pageDTO) {
        Page page = CommonMybatisPageUtil.getInstance().pageDTOtoPage(pageDTO);
        OauthClientDetailsWithRoleDTO scoreWithNamesDTO = pageDTO.getFilters();
        IPage<Map> pageList = iBaseRepository.findOauthClientDetailsWithRole(page, scoreWithNamesDTO);
        PageDTO resultPage = CommonMybatisPageUtil.getInstance().iPageToPageDTO(pageList, OauthClientDetailsWithRoleDTO.class);
        return resultPage;
    }
}