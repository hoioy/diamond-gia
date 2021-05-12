package com.hoioy.diamond.oauth2.support.pkce;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import cn.hutool.core.util.StrUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//请求/token接口时候的PKCE认证过滤器
// 处理OAuth2 客户端的认证问题，注意，不处理用户认证,用户认证由Granter来处理
public class PKCETokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {
    private final ClientDetailsService clientDetailsService;
    private final PKCEAuthorizationCodeServices authorizationCodeServices;
    private final ClientDetailsUserDetailsService clientDetailsUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public PKCETokenEndpointFilter(ClientDetailsService clientDetailsService, PKCEAuthorizationCodeServices authorizationCodeServices
            , PasswordEncoder passwordEncoder) {
        super();
        this.clientDetailsService = clientDetailsService;
        this.authorizationCodeServices = authorizationCodeServices;
        this.passwordEncoder = passwordEncoder;

        clientDetailsUserDetailsService = new ClientDetailsUserDetailsService(clientDetailsService);
        clientDetailsUserDetailsService.setPasswordEncoder(passwordEncoder);

        this.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UserDetails client = clientDetailsUserDetailsService.loadUserByUsername(authentication.getName());
                if (client == null) {
                    throw new InternalAuthenticationServiceException(
                            "UserDetailsService returned null, which is an interface contract violation");
                }
                String presentedPassword = authentication.getCredentials().toString();
                if (!passwordEncoder.matches(presentedPassword, client.getPassword())) {
                    logger.debug("Authentication failed: password does not match stored value");

                    throw new BadCredentialsException(messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
                }
                return new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(), authentication.getCredentials(),null);
            }
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!"POST".equalsIgnoreCase(request.getMethod()) && !"OPTIONS".equalsIgnoreCase(request.getMethod()) ) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[]{"POST","OPTIONS"});
        }
        // 如果已经有认证对象，说明之前的过滤器已经认证通过，则不用重复认证，跳过
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }

        String clientId = request.getParameter("client_id");

        if (clientId == null) {
            throw new BadCredentialsException("No client id presented");
        }

        clientId = clientId.trim();

        String clientSecret = request.getParameter("client_secret");
        if (StrUtil.isNotBlank(clientSecret)) {
            //如果传递了密钥，则为普通授权码模式,FORM表单认证
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId, clientSecret);
            return getAuthenticationManager().authenticate(authRequest);
        } else {
            //PKCE校验逻辑
            //如果前端传递了clientSecret，说明没有使用PKCE，则直接返回null
            //获取参数
            String codeVerifier = request.getParameter("code_verifier");
            String authorizationCode = request.getParameter("code");

            if (authorizationCode == null) {
                throw new InvalidRequestException("An authorization code must be supplied.");
            }
            if (codeVerifier == null) {
                codeVerifier = "";
            }

            //获取OAuth2 Client
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
            if (clientDetails == null) {
                throw new BadCredentialsException("PKCE校验失败：client未注册，clientId=" + clientId);
            }

            //具体认证逻辑
            OAuth2Authentication storedAuth = authorizationCodeServices.consumeAuthorizationCodeAndCodeVerifier(authorizationCode, codeVerifier);
            if (storedAuth == null) {
                throw new BadCredentialsException("PKCE校验失败：CodeVerifier不匹配=" + codeVerifier);
            }

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId,
                    codeVerifier, null);
            return authRequest;
        }
    }
}
