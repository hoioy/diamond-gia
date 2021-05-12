package com.hoioy.diamond.config;

import com.hoioy.diamond.common.service.CommonSecurityService;
import com.hoioy.diamond.oauth2.CustomUserDetailsService;
import com.hoioy.diamond.oauth2.authorization.GIAFilterSecurityInterceptor;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import com.hoioy.diamond.oauth2.support.CustomLdapAuthenticationProvider;
import com.hoioy.diamond.oauth2.support.DBAuthenticationProvider;
import com.hoioy.diamond.oauth2.support.HttpAuthenticationProvider;
import com.hoioy.diamond.security.service.BaseSecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.thymeleaf.util.ArrayUtils;

//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private GIAFilterSecurityInterceptor customFilterSecurityInterceptor;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private GIAConfig giaConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                // 自定义jwk
                .mvcMatchers("/.well-known/jwks.json").permitAll()
                .antMatchers("/oauth/**", "/login/**", "/logout", "/403", "/error").permitAll()
                .anyRequest().authenticated()   // 其他地址的访问均需验证权限
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/");

        http.cors();
//        http.csrf().disable();
        //csrf token配置
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.addFilterBefore(customFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/theme/**", "/images/**", "/css/**", "/js/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //执行顺序：Dao-》ldap-》db-》http
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

        if (ArrayUtils.contains(giaConfig.getProviderTypes(), GIAConfig.ProviderType.LDAP)) {
            auth.authenticationProvider(customLdapAuthenticationProvider());
        }

        if (ArrayUtils.contains(giaConfig.getProviderTypes(), GIAConfig.ProviderType.DB)) {
            auth.authenticationProvider(dbAuthenticationProvider());
        }

        if (ArrayUtils.contains(giaConfig.getProviderTypes(), GIAConfig.ProviderType.HTTP)) {
            auth.authenticationProvider(httpAuthenticationProvider());
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpAuthenticationProvider httpAuthenticationProvider() {
        return new HttpAuthenticationProvider();
    }

    @Bean
    public CustomLdapAuthenticationProvider customLdapAuthenticationProvider() {
        return new CustomLdapAuthenticationProvider();
    }

    @Bean
    public DBAuthenticationProvider dbAuthenticationProvider() {
        return new DBAuthenticationProvider();
    }

    //获取当前用户的工具类
    //获取当前用户的工具类
    @Bean
    public CommonSecurityService baseSecurityService() {
        return new BaseSecurityServiceImpl(passwordEncoder());
    }

}

