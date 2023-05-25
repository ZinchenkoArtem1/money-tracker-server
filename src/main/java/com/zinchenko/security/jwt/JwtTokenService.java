package com.zinchenko.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenService {

    private final UserDetailsService userDetailsService;
    private final SecretKey secret;
    private final Duration ttl;

    public JwtTokenService(UserDetailsService userDetailsService,
                           @Value("${authorization.ttl}") Duration ttl) {
        this.userDetailsService = userDetailsService;
        this.secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.ttl = ttl;
    }

    public String generateToken(String username, String role) {
        Date now = Date.from(Instant.now());

        Claims payload = Jwts.claims()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(Date.from(now.toInstant().plus(ttl.toMillis(), ChronoUnit.MILLIS)));
        payload.put("role", role);

        return Jwts.builder()
                .setClaims(payload)
                .signWith(secret)
                .compact();
    }

    public void verifyTokenAndSignature(String token) {
        Claims payload = getJwtPayload(token);
        if (payload.getExpiration().before(Date.from(Instant.now()))) {
            throw new IllegalStateException("Jwt token was expired");
        }
    }

    public Authentication getAuthentication(String token) {
        String username = getJwtPayload(token)
                .getSubject();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims getJwtPayload(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
