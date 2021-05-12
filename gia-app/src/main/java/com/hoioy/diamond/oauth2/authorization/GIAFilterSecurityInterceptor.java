package com.hoioy.diamond.oauth2.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @description 一个自定义的filter，
 * 必须包含authenticationManager,accessDecisionManager,securityMetadataSource三个属性，
 * 我们的所有控制将在这三个类中实现
 * 最核心的代码就是invoke方法中的InterceptorStatusToken token = super.beforeInvocation(fi);这一句，
 * 即在执行doFilter之前，进行权限的检查，而具体的实现已经交给accessDecisionManager
 */
@Component
public class GIAFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    @Autowired
    private GIASecurityMetadataSource securityMetadataSource;

    @Autowired
    public void setMyAccessDecisionManager(GIAAccessDecisionManager customAccessDecisionManager) {
        super.setAccessDecisionManager(customAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    private void invoke(FilterInvocation fi) throws IOException,
            ServletException {
        //fi里面有一个被拦截的url
        //里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
        //再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }
}
