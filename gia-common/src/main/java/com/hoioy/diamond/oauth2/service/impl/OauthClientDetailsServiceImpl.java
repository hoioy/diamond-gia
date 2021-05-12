package com.hoioy.diamond.oauth2.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoioy.diamond.common.base.BaseServiceImpl;
import com.hoioy.diamond.common.dto.PageDTO;
import com.hoioy.diamond.common.exception.BaseException;
import com.hoioy.diamond.common.exception.CommonException;
import com.hoioy.diamond.common.service.CommonSecurityService;
import com.hoioy.diamond.common.util.CommonBeanUtil;
import com.hoioy.diamond.common.util.CommonMybatisPageUtil;
import com.hoioy.diamond.oauth2.domain.OauthClientDetails;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsDTO;
import com.hoioy.diamond.oauth2.dto.RoleClientJoinDTO;
import com.hoioy.diamond.oauth2.mapper.OauthClientDetailsMapper;
import com.hoioy.diamond.oauth2.service.IOauthClientDetailsService;
import com.hoioy.diamond.oauth2.service.IRoleClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OauthClientDetailsServiceImpl extends BaseServiceImpl<OauthClientDetailsMapper, OauthClientDetails, OauthClientDetailsDTO>
        implements IOauthClientDetailsService<OauthClientDetails> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OauthClientDetailsServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleClientService iRoleClientService;

    @Override
    public PageDTO getPage(PageDTO pageDTO) throws BaseException {
        Page page = CommonMybatisPageUtil.getInstance().pageDTOtoPage(pageDTO);
        OauthClientDetails course = getDomainFilterFromPageDTO(pageDTO);
        IPage<OauthClientDetails> courseIPage = iBaseRepository.selectPage(page, course);
        PageDTO resultPage = CommonMybatisPageUtil.getInstance().iPageToPageDTO(courseIPage, OauthClientDetailsDTO.class);
        return resultPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(OauthClientDetailsDTO dto, Set<String> roleNames) throws JsonProcessingException {

        LOGGER.debug("add client");
        if (null != dto) {
            saveOrUpdate(dto);
            // 新增
            OauthClientDetails oauthClientDetails = new OauthClientDetails();
            //dto.setFlag(1);// 初始化
            BeanUtils.copyProperties(dto, oauthClientDetails);
            oauthClientDetails.setAdditionalInformation(
                    new ObjectMapper().writeValueAsString(dto.getAdditionalInformation()));
            this.iBaseRepository.insert(oauthClientDetails);

            List<RoleClientJoinDTO> roleClientJoinDTOS = new ArrayList<>();
            //默认给当前用户的角色赋予权限
            roleNames.forEach(roleName -> {
                if (roleName.startsWith("ROLE_")) {
                    RoleClientJoinDTO roleClientJoinDTO = new RoleClientJoinDTO();
                    roleClientJoinDTO.setClientId(dto.getClientId());
                    roleClientJoinDTO.setRoleName(roleName);
                    roleClientJoinDTOS.add(roleClientJoinDTO);
                }
            });

            iRoleClientService.batchCreate(roleClientJoinDTOS);
        }
    }

    @Override
    public List<OauthClientDetailsDTO> findAll() {
        List<OauthClientDetails> list = mybatisPlusServiceImpl.list();
        return domainListToDTOList(list);
    }

    @Override
    public OauthClientDetailsDTO findByClientId(String clientId) {
        OauthClientDetails domain = iBaseRepository.findByClientId(clientId);
        if (domain == null) {
            return null;
        }
        return domainToDTO(domain);
    }

    @Override
    public List<OauthClientDetailsDTO> findAllByClientIdIn(List<String> clientIds) {
        return domainListToDTOList(iBaseRepository.findAllByClientIdIn(clientIds));
    }

    @Override
    public OauthClientDetailsDTO update(OauthClientDetailsDTO dto) {
        LOGGER.debug("add client");
        saveOrUpdate(dto);

        OauthClientDetails oauthClientDetails = iBaseRepository.selectById(dto.getId());
        if (oauthClientDetails != null) {
            oauthClientDetails.setModifiedBy(CommonSecurityService.instance.getCurrentLoginName());
            oauthClientDetails.setModifiedDate(LocalDateTime.now());
            try {
                oauthClientDetails.setAdditionalInformation(new ObjectMapper().writeValueAsString(dto.getAdditionalInformation()));
            } catch (JsonProcessingException e) {
                throw new CommonException("修改失败");
            }
            CommonBeanUtil.updateCopy(dto, oauthClientDetails);
            iBaseRepository.updateById(oauthClientDetails);

            return domainToDTO(oauthClientDetails, false);
        } else {
            throw new CommonException("对象不存在");
        }

    }

    private void saveOrUpdate(OauthClientDetailsDTO dto) throws BaseException {
        if (null != dto) {
            if (dto.getRefreshTokenValidity() == null) {
                dto.setRefreshTokenValidity(72000);
            }
            if (dto.getAccessTokenValidity() == null) {
                dto.setAccessTokenValidity(7200);
            }
//            if(StrUtil.isNotBlank(dto.getClientId())){
//                dto.setId(dto.getClientId());
//            }
            if (StrUtil.isBlank(dto.getAuthorizedGrantTypes())
                    || !dto.getAuthorizedGrantTypes().contains("refresh_token")) {
                //如果不存在refresh_token，则补上
                dto.setAuthorizedGrantTypes(dto.getAuthorizedGrantTypes() + ",refresh_token");
            }

            if (StrUtil.isBlank(dto.getMainHost())) {
                throw new CommonException("mainHost应用主页不能为null");
            }

            dto.setClientSecret(passwordEncoder.encode(dto.getClientSecret()));

            Map<String, String> map = new HashMap();
            map.put(OauthClientDetailsDTO.CUSTOMNAMEFIELD, dto.getCustomName());
            map.put(OauthClientDetailsDTO.CUSTOMSUBNAMEFIELD, dto.getCustomSubName());
            map.put(OauthClientDetailsDTO.ICONFIELD, dto.getIcon());
            map.put(OauthClientDetailsDTO.MAINHOST, dto.getMainHost());
            dto.setAdditionalInformation(map);
        }
    }

    @Override
    public OauthClientDetailsDTO domainToDTO(OauthClientDetails domain, Boolean isCopyEmptyField) {
        OauthClientDetailsDTO dto = super.domainToDTO(domain, isCopyEmptyField);
        if (StrUtil.isNotBlank(domain.getAdditionalInformation())) {
            Map map = null;
            try {
                map = new ObjectMapper().readValue(domain.getAdditionalInformation(), Map.class);
                dto.setAdditionalInformation(map);
                dto.setIcon((String) map.get(OauthClientDetailsDTO.ICONFIELD));
                dto.setCustomSubName((String) map.get(OauthClientDetailsDTO.CUSTOMSUBNAMEFIELD));
                dto.setCustomName((String) map.get(OauthClientDetailsDTO.CUSTOMNAMEFIELD));
                dto.setMainHost((String) map.get(OauthClientDetailsDTO.MAINHOST));
            } catch (IOException e) {
                log.error(e.toString());
                return null;
            }
        }

        return dto;
    }
}