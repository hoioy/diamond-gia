package com.hoioy.diamond.oauth2.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hoioy.diamond.common.base.IBaseMapper;
import com.hoioy.diamond.oauth2.domain.OauthClientDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OauthClientDetailsMapper extends IBaseMapper<OauthClientDetails> {

    /**
     * 分页
     */
    IPage<OauthClientDetails> selectPage(@Param("page") Page page, @Param("oauthClientDetails") OauthClientDetails oauthClientDetails);

    OauthClientDetails findByClientId(String clientId);

    List<OauthClientDetails> findAllByClientIdIn(List<String> clientIds);
}
