package com.hoioy.diamond.oauth2.service;

import com.hoioy.diamond.common.domain.CommonDomain;
import com.hoioy.diamond.common.service.IBaseService;
import com.hoioy.diamond.oauth2.domain.OauthToken;
import com.hoioy.diamond.oauth2.dto.OauthTokenDTO;

import java.util.List;

public interface IOauthTokenService<D extends CommonDomain> extends IBaseService<OauthTokenDTO, OauthToken> {

    Long onLineUserCount();

    List<OauthTokenDTO> findAllByJtiIn(List<String> jtis);
}
