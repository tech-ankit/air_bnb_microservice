package com.gateway.config;

import com.gateway.payload.TokenResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTFilter implements WebFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authorization != null && authorization.startsWith("Bearer")){
            String token = authorization.substring(7);
            try {
                RestTemplate restTemplate = new RestTemplate();
                TokenResponseDto tokenResponse = restTemplate.getForObject("http://localhost:8000/api/v1/user/token/validation?token="+token,TokenResponseDto.class);
                if(tokenResponse != null){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(tokenResponse,null, Collections.singleton(new SimpleGrantedAuthority(tokenResponse.getRole())));
                    SecurityContext securityContext =  new SecurityContextImpl(authToken);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return chain.filter(exchange);
    }
}
