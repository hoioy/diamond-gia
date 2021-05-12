package com.hoioy.diamond.oauth2.config;

import com.hoioy.diamond.common.util.CommonRedisUtil;
import com.hoioy.diamond.oauth2.support.CustomJwtAccessTokenConverter;
import com.hoioy.diamond.oauth2.support.CustomJwtTokenStore;
import com.hoioy.diamond.oauth2.support.CustomTokenServices;
import com.hoioy.diamond.oauth2.support.pkce.PKCEAuthorizationCodeServices;
import com.hoioy.diamond.oauth2.support.pkce.PKCEAuthorizationCodeTokenGranter;
import com.hoioy.diamond.oauth2.support.pkce.PKCETokenEndpointFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerJWTTokenStoreConfig extends AuthorizationServerConfigurerAdapter {
    //同一用户最多允许同时登录个数,自定义配置,默认20000
    @Value("${base.oauth2.server.maximumConcurrentUser:20000}")
    private Integer maximumConcurrentUser;

    private KeyPair keyPair;

    @Resource
    private DataSource dataSource;

    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 注入authenticationManager来支持 password grant type
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CommonRedisUtil redisUtil;

    public AuthorizationServerJWTTokenStoreConfig(KeyPair keyPair) throws Exception {
        this.keyPair = keyPair;
    }

    /**
     * 配置ClientDetailsService
     * 注意，除非你在下面的configure(AuthorizationServerEndpointsConfigurer)中指定了一个AuthenticationManager，否则密码授权方式不可用。
     * 至少配置一个client，否则服务器将不会启动。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    /**
     * 配置授权服务器的安全，意味着实际上是/oauth/token端点。
     * /oauth/authorize端点也应该是安全的
     * 默认的设置覆盖到了绝大多数需求，所以一般情况下你不需要做任何事情。
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        super.configure(oauthServer);
        // 默认tokenKeyAccess和checkTokenAccess对应端口权限是denyAll(),如果允许资源服务器调用这些端口，则需要覆盖默认配置
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        //增加PKCE的filter，支持post形式的client认证，包含了allowFormAuthenticationForClients,//省略oauthServer.allowFormAuthenticationForClients();
        oauthServer.addTokenEndpointAuthenticationFilter(pkceTokenEndpointFilter());
        // 解决OPTION /oauth/token 请求跨域问题
        oauthServer.addTokenEndpointAuthenticationFilter(new CorsFilter(corsConfigurationSource()));
    }

    /**
     * 该方法是用来配置Authorization Server endpoints的一些非安全特性的，比如token存储、token自定义、授权类型等等的
     * 默认情况下，你不需要做任何事情，除非你需要密码授权，那么在这种情况下你需要提供一个AuthenticationManager
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(jwtAccessTokenConverter());
        //注入authenticationManager来支持 password grant type
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenServices(this.customTokenServices());
        endpoints.requestFactory(defaultOAuth2RequestFactory());
        endpoints.authorizationCodeServices(pkceAuthorizationCodeServices());
        endpoints.tokenGranter(tokenGranter(endpoints));
        //用于自定义授权确认界面,路径对应接口在HomeController的getAccessConfirmation方法定义
        //TODO cloud没有自定义这个页面，所以暂时注释掉
//        endpoints.pathMapping("/oauth/confirm_access", "/custom/oauth/confirm_access");
    }

    @Bean
    public PKCEAuthorizationCodeServices pkceAuthorizationCodeServices() {
        return new PKCEAuthorizationCodeServices(clientDetailsService, passwordEncoder);
    }

    @Bean
    public PKCETokenEndpointFilter pkceTokenEndpointFilter() {
        PKCETokenEndpointFilter pkceTokenEndpointFilter = new PKCETokenEndpointFilter(clientDetailsService, pkceAuthorizationCodeServices(), passwordEncoder);
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setTypeName("Form");
        authenticationEntryPoint.setRealmName("oauth2/client");
        pkceTokenEndpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
        return pkceTokenEndpointFilter;
    }

    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<>();
        granters.add(new RefreshTokenGranter(customTokenServices(), clientDetailsService, defaultOAuth2RequestFactory()));
        granters.add(new ImplicitTokenGranter(customTokenServices(), clientDetailsService, defaultOAuth2RequestFactory()));
        granters.add(new ClientCredentialsTokenGranter(customTokenServices(), clientDetailsService, defaultOAuth2RequestFactory()));
        granters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, customTokenServices(),
                clientDetailsService, defaultOAuth2RequestFactory()));
        //使用自定义PKCE授权类型替换默认的AuthorizationCodeTokenGranter
        granters.add(new PKCEAuthorizationCodeTokenGranter(customTokenServices(), pkceAuthorizationCodeServices()
                , clientDetailsService, defaultOAuth2RequestFactory()));

        return new CompositeTokenGranter(granters);
    }

    @Bean
    public DefaultOAuth2RequestFactory defaultOAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    @Primary
    public DefaultTokenServices customTokenServices() {
        DefaultTokenServices tokenServices = new CustomTokenServices();
        ((CustomTokenServices) tokenServices).setRedisUtil(redisUtil);
        tokenServices.setTokenStore(customJwtTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        tokenServices.setClientDetailsService(clientDetailsService);
        ((CustomTokenServices) tokenServices).setMaximumConcurrentUser(maximumConcurrentUser);
        return tokenServices;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        CustomJwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();
        converter.setKeyPair(this.keyPair);
        return converter;
    }

    @Bean
    public TokenStore customJwtTokenStore() {
        return new CustomJwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 允许所有方法包括"GET", "POST", "DELETE", "PUT"等等
        corsConfiguration.setMaxAge(1800l);//30分钟
        //使用setAllowCredentials的方式解决跨域问题只支持ie10以上。
        //如果使用默认的配合CookierHttpSessionStrategy的session方式
        // 前后端一起开启，开启之后就能够读写浏览器的Cookies。该字段可选。它的值是一个布尔值，表示是否允许发送Cookie。
        // 默认情况下，Cookie不包括在CORS请求之中。设为true，即表示服务器明确许可，
        // Cookie可以包含在请求中，一起发给服务器。这个值也只能设为true，如果服务器不要浏览器发送Cookie，删除该字段即可。
        corsConfiguration.setAllowCredentials(true);
        //配合HeaderHttpSessionStrategy的session方式。token的方式解决跨域问题。
        // CORS请求时。XMLHttpRequest对象的getResponseHeader()方法只能拿到6个基本字段：Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma。如果想拿到其他字段，
        // 就必须在Access-Control-Expose-Headers里面指定。上面的例子指定，getResponseHeader(‘FooBar’)可以返回FooBar字段的值。
        //允许clienHeaderWriterFiltert-site取得自定义得header值
        corsConfiguration.addExposedHeader(HttpHeaders.AUTHORIZATION);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}



