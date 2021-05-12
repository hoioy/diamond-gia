package com.hoioy.diamond.oauth2.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hoioy.diamond.common.base.IBaseMapper;
import com.hoioy.diamond.oauth2.domain.OauthToken;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OauthTokenMapper extends IBaseMapper<OauthToken> {

    /**
     * 分页
     */
    IPage<OauthToken> selectPage(@Param("page") Page page, @Param("oauthToken") OauthToken oauthToken);

    //    @Query("SELECT count(distinct sub) FROM OauthToken where expiration > :currentTime")
    Long findOnLineUserCount(Long currentTime);//使用java时间，因为数据库中插入数据时候也使用得是java时间。需要注意时间比较时候的统一性

    List<OauthToken> findAllByJtiIn(List<String> jtis);
}
