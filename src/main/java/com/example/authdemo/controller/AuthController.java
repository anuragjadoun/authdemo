package com.example.authdemo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User user) {
        Map<String, Object> data = new HashMap<>();

        if (user == null) {
            data.put("authenticated", false);
            return data;
        }

        data.put("authenticated", true);
        data.put("name", user.getAttribute("name"));
        data.put("email", user.getAttribute("email"));
        data.put("picture", user.getAttribute("picture"));

        return data;
    }
}
