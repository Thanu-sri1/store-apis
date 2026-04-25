package com.codewithmosh.store.auth.service;

import java.util.Date;

import javax.crypto.SecretKey;

import com.codewithmosh.store.auth.config.JwtConfig;
import com.codewithmosh.store.auth.entities.Role;
import com.codewithmosh.store.auth.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Jwt {

    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public Long getId() {
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole() {
        String roleString = claims.get("role", String.class);
        return Role.valueOf(roleString);
    }

    public Boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    @Override
    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
