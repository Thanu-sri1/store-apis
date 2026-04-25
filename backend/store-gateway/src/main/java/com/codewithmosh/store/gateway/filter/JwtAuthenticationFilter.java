package com.codewithmosh.store.gateway.filter;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.codewithmosh.store.gateway.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtConfig jwtConfig;

    // Paths that don't require authentication
    private static final List<String> PUBLIC_PATHS = List.of(
            "/login",
            "/login/refresh",
            "/users",        // POST only - handled by role check downstream
            "/v3/api-docs",
            "/swagger-ui",
            "/checkout/webhook"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var path = request.getURI().getPath();
        var method = request.getMethod().name();

        // Allow public paths
        if (isPublicPath(path, method)) {
            return chain.filter(exchange);
        }

        var authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var token = authHeader.replace("Bearer ", "");
        var claims = parseClaims(token);

        if (claims == null || claims.getExpiration().before(new Date())) {
            log.warn("Invalid or expired JWT token for path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Inject user identity headers for downstream services
        var userId = claims.getSubject();
        var role = claims.get("role", String.class);

        var mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();

        log.debug("Authenticated user {} with role {} accessing {}", userId, role, path);

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublicPath(String path, String method) {
        // POST /users is public (registration)
        if ("POST".equals(method) && path.startsWith("/users")) return true;
        // Checkout webhook is public (Stripe calls it)
        if (path.startsWith("/checkout/webhook")) return true;
        // Login endpoints are always public
        if (path.startsWith("/login")) return true;
        // Swagger / OpenAPI
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) return true;
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.warn("JWT parsing failed: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public int getOrder() {
        return -1; // Run before all other filters
    }
}
