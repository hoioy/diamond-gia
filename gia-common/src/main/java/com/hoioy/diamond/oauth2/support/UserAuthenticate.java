package com.hoioy.diamond.oauth2.support;


import com.hoioy.diamond.oauth2.config.GIAConfig;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * TODO： v1版本自定义ldap ，考虑使用Spring security 原生的LdapAuthenticationProvider
 */
@Service
public class UserAuthenticate {
    private static final Logger logger = LoggerFactory.getLogger(AbstractUserDetailsService.class);

    @Autowired
    private GIAConfig GIAConfigure;

    private String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private LdapContext ctx = null;
    private Hashtable env = null;
    private Control[] connCtls = null;

    public UserAuthenticate() {
        super();
    }

    private void LDAP_connect() {
        this.env = new Hashtable();
        this.env.put("java.naming.factory.initial", this.FACTORY);
        this.env.put("java.naming.provider.url", GIAConfigure.getURL() + GIAConfigure.getBASEDN());
        this.env.put("java.naming.security.authentication", "simple");
        this.env.put("java.naming.security.principal", GIAConfigure.getAdminName());
        this.env.put("java.naming.security.credentials", GIAConfigure.getAdminPassword());

        try {
            this.ctx = new InitialLdapContext(this.env, this.connCtls);
        } catch (AuthenticationException e) {
            logger.error("Authentication faild: AuthenticationException " + e.getMessage()+"e={}",e);
            System.out.println();
        } catch (Exception e) {
            logger.error("Authentication faild: Exception " + e.getMessage()+"e={}",e);
        }

    }

    public String getUserDN(String email) {
        String userDN = "";
        System.out.println(email);
        this.LDAP_connect();

        try {
            SearchControls e = new SearchControls();
            e.setSearchScope(2);
            NamingEnumeration en = this.ctx.search("", "mail=" + email, e);
            if (en == null) {
                logger.warn("There's no NamingEnumeration.");
            }

            if (!en.hasMoreElements()) {
                logger.warn("There's no element.");
            }

            for (; en != null && en.hasMoreElements(); System.out.println()) {
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    String employeeId = "";
                    if (si.getAttributes().get("employeeid") != null) {
                        employeeId = si.getAttributes().get("employeeid").toString().replaceAll(" ", "").trim().replace(":", "=");
                    }

                    userDN = userDN + si.getName();
                    userDN = userDN + "," + GIAConfigure.getBASEDN() + "," + employeeId;

                } else {
                    logger.info("{}",obj);
                }
            }
        } catch (Exception var7) {
            logger.warn("Exception in search():" + var7);
        }

        return userDN;
    }

    public Map authenricateName(String ID, String password) {
        HashMap map = new HashMap();
        String userDN = this.getUserDN(ID);
        if (StrUtil.isEmpty(userDN)) {
            throw new AuthenticationServiceException("系统找不到此用户");
        }

        String realUserDN = userDN.substring(0, userDN.lastIndexOf(","));
        try {
            this.ctx.addToEnvironment("java.naming.security.principal", realUserDN);
            this.ctx.addToEnvironment("java.naming.security.credentials", password);
            this.ctx.reconnect(this.connCtls);
            if (userDN != "") {
                String[] e = userDN.split(",");
                String[] var6 = e;
                int var7 = e.length;

                for (int var8 = 0; var8 < var7; ++var8) {
                    String s = var6[var8];
                    String[] ms = s.split("=");
                    map.put(ms[0], ms[1]);
                }
            }
        } catch (AuthenticationException var11) {
            System.out.println(userDN + " is not authenticated");
            System.out.println(var11.toString());
        } catch (NamingException var12) {
            System.out.println(userDN + " is not authenticated");
        }

        return map;
    }

    public boolean authenricate(String ID, String password) {
        boolean valide = false;
        String userDN = this.getUserDN(ID);

        try {
            this.ctx.addToEnvironment("java.naming.security.principal", userDN);
            this.ctx.addToEnvironment("java.naming.security.credentials", password);
            this.ctx.reconnect(this.connCtls);
            if (userDN.isEmpty()) {
                valide = false;
                System.out.println(userDN + " is authenticated");
            }

            valide = true;
        } catch (AuthenticationException var6) {
            System.out.println(userDN + " is not authenticated");
            System.out.println(var6.toString());
            valide = false;
        } catch (NamingException var7) {
            System.out.println(userDN + " is not authenticated");
            valide = false;
        }

        return valide;
    }
}
