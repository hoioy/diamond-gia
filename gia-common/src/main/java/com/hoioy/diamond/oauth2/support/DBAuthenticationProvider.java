package com.hoioy.diamond.oauth2.support;

import com.hoioy.diamond.common.dto.ResultDTO;
import com.hoioy.diamond.log.service.IWebLogsService;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 外接数据库用户源
 */
@Component
public class DBAuthenticationProvider implements AuthenticationProvider ,ICustomProvider{
    private static final Logger log = LoggerFactory.getLogger(DBAuthenticationProvider.class);

    @Autowired
    private GIAConfig giaConfig;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IWebLogsService iWebLogsService;

    public void initDB() {
        // JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(giaConfig.getDriverClassName());
        dataSource.setUrl(giaConfig.getDbUrl());
        dataSource.setUsername(giaConfig.getUsername());
        dataSource.setPassword(giaConfig.getPassword());
        // 创建JDBC模板
        jdbcTemplate = new JdbcTemplate();
        // 这里也可以使用构造方法
        jdbcTemplate.setDataSource(dataSource);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        if (StrUtil.isEmpty(username)) {
            throw new AuthenticationServiceException("User name can't be null");
        }

        Long num = getUserCount(username, password);

        if (num < 1) {
            throw new AuthenticationServiceException("登录名称或密码不正确，请重新输入");
        }

        iWebLogsService.create(getLoginLogDTO(username, GIAConfig.ProviderType.DB));

        //TODO 获得用户的权限上下文，ldap用户只有访问portal的权限
        List<String> as = ListUtil.toList(giaConfig.getDefaultAuthorityList());
        as.add(giaConfig.providerTypeAuthorityName + GIAConfig.ProviderType.DB);
        List<SimpleGrantedAuthority> authorities = initAuthorities(as);

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(), authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    public Long getUserCount(String username, String password) {
        String sql = "select count(*) " + sqlFrom(username, password);
        if (jdbcTemplate == null) {
            initDB();
        }

        Long num = (long) jdbcTemplate.queryForObject(sql, Long.class);
        return num;
    }

    public Map<String, String> getUser(String username) {
        String sql = "select * " + sqlFrom(username);
        if (jdbcTemplate == null) {
            initDB();
        }

        Map<String, Object> user = jdbcTemplate.queryForMap(sql);
        Map<String, String> newMap = new LinkedHashMap<>();

        newMap.put("name", username);
        newMap.put("email", (String) user.get("email"));
        return newMap;
    }

    public ResultDTO changePassword(String username, String password, String newPassword) {
        ResultDTO resultDTO = new ResultDTO();

        Long num = getUserCount(username, password);

        if (num < 1) {
            resultDTO.setCode(200);
            resultDTO.setMessage("登录名称或密码不正确，请重新输入");
        }

        String sql = "update " + giaConfig.getTableName() + " set password='" + newPassword + "' "
                + " where " + sqlUserNameWhere(username) + " and " + sqlPasswordWhere(password);

        if (jdbcTemplate == null) {
            initDB();
        }

        Integer updateCount = jdbcTemplate.update(sql);
        resultDTO.setCode(200);
        resultDTO.setData(updateCount);
        resultDTO.setMessage("更新密码成功");

        return resultDTO;
    }

    private String sqlFrom(String username) {
        return sqlFrom() + " where " + sqlUserNameWhere(username);
    }

    private String sqlFrom(String username, String password) {
        return sqlFrom() + " where " + sqlUserNameWhere(username) + " and " + sqlPasswordWhere(password);
    }

    private String sqlFrom() {
        return " from " + giaConfig.getTableName();
    }

    private String sqlUserNameWhere(String username) {
        return giaConfig.getNameField() + "='" + username + "' ";
    }

    private String sqlPasswordWhere(String password) {
        return giaConfig.getCredentialField() + "='" + password + "' ";
    }
}
