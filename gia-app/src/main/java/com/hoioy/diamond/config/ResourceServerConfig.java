package com.hoioy.diamond.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.requestMatchers().antMatchers( "/exit","/user/**","/me")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();

        // TODO  http.csrf().ignoringAntMatchers("/actuator/**"); 可以被tdf-cloud-admin-server监控，需要禁用csrf .post 类型请求csrf临时禁用http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.csrf().disable();

        http.cors();
    }

}
