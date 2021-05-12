package com.hoioy.diamond.oauth2.service.impl;

import com.hoioy.diamond.common.base.BaseServiceImpl;
import com.hoioy.diamond.oauth2.domain.OauthToken;
import com.hoioy.diamond.oauth2.dto.OauthTokenDTO;
import com.hoioy.diamond.oauth2.mapper.OauthTokenMapper;
import com.hoioy.diamond.oauth2.service.IOauthTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OauthTokenServiceImpl extends BaseServiceImpl<OauthTokenMapper, OauthToken, OauthTokenDTO> implements IOauthTokenService<OauthToken> {
    private static final Logger logger = LoggerFactory.getLogger(OauthTokenServiceImpl.class);

    @Override
    public Long onLineUserCount() {
        logger.debug("onLineUserCount");
        return iBaseRepository.findOnLineUserCount(new Date().getTime());
    }

    @Override
    public List<OauthTokenDTO> findAllByJtiIn(List<String> jtis) {
        return domainListToDTOList(iBaseRepository.findAllByJtiIn(jtis));
    }

}
