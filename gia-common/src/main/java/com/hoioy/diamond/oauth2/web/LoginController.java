package com.hoioy.diamond.oauth2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}