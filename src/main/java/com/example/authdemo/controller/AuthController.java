package com.example.authdemo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/login-success")
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User oauthUser) {

        Map<String, Object> response = new HashMap<>();
        response.put("message", "OAuth Login Successful ");
        response.put("email", oauthUser.getAttribute("email"));
        response.put("name", oauthUser.getAttribute("name"));
        response.put("picture", oauthUser.getAttribute("picture"));

        return response;
    }
}
