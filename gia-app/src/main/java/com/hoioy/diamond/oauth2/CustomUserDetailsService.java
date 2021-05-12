package com.hoioy.diamond.oauth2;

import com.hoioy.diamond.dto.RoleDTO;
import com.hoioy.diamond.dto.UserDTO;
import com.hoioy.diamond.log.service.IWebLogsService;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import com.hoioy.diamond.oauth2.support.AbstractUserDetailsService;
import com.hoioy.diamond.security.BaseSecurityUser;
import com.hoioy.diamond.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService extends AbstractUserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IWebLogsService iWebLogsService;

    /**
     * 授权的时候是对角色授权，而认证的时候应该基于资源，而不是角色，因为资源是不变的，而用户的角色是会变的
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO sysUser = userService.findByLoginName(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException(username);
        }

        if (!"1".equals(sysUser.getState())) {
            throw new UsernameNotFoundException("该用户处于锁定状态");
        }

        List<String> as = new ArrayList<>();
        // 以权限名封装为Spring的security Object，放入role，role是权限
        Set<RoleDTO> roleDTOs = sysUser.getRoles();
        for (RoleDTO roleDTO : roleDTOs) {
            //同时放入name和id
            as.add(roleDTO.getRoleName());
            as.add(roleDTO.getId());
        }
        List<SimpleGrantedAuthority> authorities = initAuthorities(as);

        iWebLogsService.create(getLoginLogDTO(username, GIAConfig.ProviderType.SELF));

        boolean enables = true; // 是否可用
        boolean accountNonExpired = true; // 是否过期
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        /** 连接数据库根据登陆？？用户名称获得用户信息 */
        // 封装成spring security的user
        BaseSecurityUser userdetail = new BaseSecurityUser(sysUser, sysUser.getLoginName(), sysUser.getPassword(),
                enables, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);

        return userdetail;
    }

}
