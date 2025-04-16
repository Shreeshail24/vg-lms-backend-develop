package com.samsoft.lms.approvalSettings.entities;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class JwtUtil {

    @Autowired
    private HttpServletRequest request;

    public Optional<String> getCurrentUserJWT() {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7)); // Remove "Bearer " prefix
        }
        return Optional.empty();
    }
}