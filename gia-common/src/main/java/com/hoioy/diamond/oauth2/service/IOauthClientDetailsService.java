package com.hoioy.diamond.oauth2.service;

import com.hoioy.diamond.common.domain.CommonDomain;
import com.hoioy.diamond.common.service.IBaseService;
import com.hoioy.diamond.oauth2.domain.OauthClientDetails;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

public interface IOauthClientDetailsService<D extends CommonDomain> extends IBaseService<OauthClientDetailsDTO, OauthClientDetails> {

    void add(OauthClientDetailsDTO dto, Set<String> currentUserRoleIds) throws JsonProcessingException;

    List<OauthClientDetailsDTO> findAll();

    OauthClientDetailsDTO findByClientId(String clientId);

    List<OauthClientDetailsDTO> findAllByClientIdIn(List<String> clientIds);
}
