package com.hoioy.diamond.oauth2.webapi;

import com.hoioy.diamond.common.dto.ResultDTO;
import io.swagger.annotations.Api;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@SessionAttributes("authorizationRequest")
@Api(tags = {"退出"})
public class LogoutController {
    @GetMapping("/exit")
    @CrossOrigin
    public ResultDTO exit(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
        return new ResultDTO("Logout Success！");
    }
}
