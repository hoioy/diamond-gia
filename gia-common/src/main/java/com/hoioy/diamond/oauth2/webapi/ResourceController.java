package com.hoioy.diamond.oauth2.webapi;

import com.hoioy.diamond.oauth2.config.GIAConfig;
import com.hoioy.diamond.oauth2.support.DBAuthenticationProvider;
import com.hoioy.diamond.oauth2.support.HttpAuthenticationProvider;
import com.hoioy.diamond.oauth2.support.UserAuthenticate;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 获得认证信息，认证通过后，第三方应用可以请求的资源
 */
@RestController
@Api(tags = {"获取OAuth2相关资源"})
public class ResourceController {
    public static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private UserAuthenticate auth;

    @Autowired
    private DBAuthenticationProvider dbAuthenticationProvider;

    @Autowired
    private HttpAuthenticationProvider httpAuthenticationProvider;

    @RequestMapping("/me")
    @CrossOrigin
    public Object user2(Principal principal) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
        Authentication authentication = oAuth2Authentication.getUserAuthentication();
        if (authentication.getAuthorities() != null) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if ((GIAConfig.providerTypeAuthorityName + GIAConfig.ProviderType.LDAP).equals(grantedAuthority.getAuthority())) {
                    return genericUserFromLDAP(principal);
                } else if ((GIAConfig.providerTypeAuthorityName + GIAConfig.ProviderType.DB).equals(grantedAuthority.getAuthority())) {
                    return genericUserFromDB(principal);
                } else if ((GIAConfig.providerTypeAuthorityName + GIAConfig.ProviderType.HTTP).equals(grantedAuthority.getAuthority())) {
                    return genericUserFromHTTP(principal);
                }
            }
        }

        //自带数据库中的用户
//        return genericUserFromSelf(principal);
        return principal;
    }

    private Map<String, Object> genericUserFromSelf(Principal principal) {
        //自带数据库中查询用户
        Map<String, Object> newMap = new LinkedHashMap<>();
        newMap.put("name", principal.getName());
        newMap.put("login", principal.getName());
//        UserDTO userDTO = userService.findByLoginName(principal.getName());
//        newMap.put("email", userDTO.getEmail());
        return newMap;
    }


    private Map<String, Object> genericUserFromLDAP(Principal principal) {
        //copy from 豆
        Map<String, Object> newMap = new LinkedHashMap<>();
        Map<String, Object> map = new LinkedHashMap<>();

        String username = principal.getName().trim();
        String loginName = "";
        if (username.indexOf("@") > 0) {
            //包含邮箱地址
            loginName = username.split("@")[0];
        } else {
            loginName = username;
            username = username + "@qq.com";
        }

        String authmap = this.auth.getUserDN(username);
        System.out.println("authmap" + authmap);
        if (!authmap.isEmpty()) {
            String[] e = authmap.split(",");
            String[] var6 = e;
            int var7 = e.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                String s = var6[var8];
                String[] ms = s.split("=");
                //避免key相同而被
                map.put(ms[0] + var8, ms[1]);
            }
        }

        //CN0=,OU1=,OU2=,OU3=AAA,DC4=aaa,DC5=com,DC6=CN,employeeID7=BT4969
        newMap.put("name", map.get("CN0"));
        newMap.put("email", username);
        //部门
        newMap.put("deptName3", map.get("OU1"));
        newMap.put("deptName2", map.get("OU2"));
        newMap.put("deptName1", map.get("OU3"));
        newMap.put("employeeID", map.get("employeeID7"));
        newMap.put("username", loginName);
        newMap.put("loginName", loginName);

        //通过service添加注解
//        logger.info("获取的id是： "+newMap.get("id"));
        return newMap;
    }

    private Map genericUserFromDB(Principal principal) {
        Map map = dbAuthenticationProvider.getUser(principal.getName());
        return map;
    }

    private Map genericUserFromHTTP(Principal principal) {
        Map map = httpAuthenticationProvider.getUser(principal.getName());
        return map;
    }
}
