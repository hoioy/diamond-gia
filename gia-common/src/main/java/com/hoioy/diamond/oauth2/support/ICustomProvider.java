package com.hoioy.diamond.oauth2.support;

import com.hoioy.diamond.log.dto.WebLogsDTO;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface ICustomProvider {
    default WebLogsDTO getLoginLogDTO(String loginname, GIAConfig.ProviderType providerType) {
        WebLogsDTO log = new WebLogsDTO();

        log.setLogUserName(loginname);
        log.setModule("统一认证");
        log.setLogOperationType("用户登录");
        log.setLogInfo(providerType.name());
        log.setStartTime(LocalDateTime.now());
        log.setEndTime(LocalDateTime.now());
        log.setFlag(1);

        return log;
    }

    default List<SimpleGrantedAuthority> initAuthorities(List<String> roleNameOrIds) {
        //TODO 获得用户的权限上下文，ldap用户只有访问portal的权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roleNameOrIds.forEach(nameOrId -> {
            //同时放入id和name，因为分布式环境下有的时候根据id，有的时候根据name鉴权
            authorities.add(new SimpleGrantedAuthority(nameOrId));
        });
        return authorities;
    }
}
