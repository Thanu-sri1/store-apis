package com.codewithmosh.store.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.codewithmosh.store.auth.config.JwtConfig;
import com.codewithmosh.store.auth.dto.JwtTokenDto;
import com.codewithmosh.store.auth.dto.LoginUserDto;
import com.codewithmosh.store.auth.dto.UserDto;
import com.codewithmosh.store.auth.mappers.UserMapper;
import com.codewithmosh.store.auth.repositories.UserRepository;
import com.codewithmosh.store.auth.service.Jwt;
import com.codewithmosh.store.auth.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping
    public ResponseEntity<JwtTokenDto> loginUser(
            @Valid @RequestBody LoginUserDto request,
            HttpServletResponse response
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var jwtToken = jwtService.generateJwtToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/login/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTockenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtTokenDto(jwtToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenDto> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jwt.getId();
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateJwtToken(user);

        return ResponseEntity.ok(new JwtTokenDto(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader
    ) {
        Long userId;
        try {
            userId = Long.parseLong(userIdHeader);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
