package com.zinchenko.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class AuthFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;
    private final String authorizationHeader;

    public AuthFilter(JwtTokenService jwtTokenService,
                      @Value("${authorization.header}") String authorizationHeader) {
        this.jwtTokenService = jwtTokenService;
        this.authorizationHeader = authorizationHeader;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = ((HttpServletRequest) servletRequest).getHeader(authorizationHeader);
        try {
            jwtTokenService.verifyTokenAndSignature(token);
            Authentication authentication = jwtTokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exc) {
            SecurityContextHolder.clearContext();
            throw exc;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
