package com.efundzz.crmservice.security;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import javax.servlet.http.HttpServletRequest;

public class CustomBearerTokenResolver implements BearerTokenResolver {
    private final BearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final String[] PUBLIC_ENDPOINTS = new String[]{"/api/applications"};

    @Override
    public String resolve(HttpServletRequest request) {
        for (String publicEndpoint : PUBLIC_ENDPOINTS) {
            if (pathMatcher.match(publicEndpoint, request.getServletPath())) {
                return null;
            }
        }
        return defaultResolver.resolve(request);
    }
}
