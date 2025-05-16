package com.example.library.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class TokenAuthInterceptor implements HandlerInterceptor {

    private static final long  TTL_EXTENSION_MIN = 1;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
            return false;
        }

        String token = authHeader.substring(7);
        String redisKey =token;
        String username = redisTemplate.opsForValue().get(token);

        Boolean exists = redisTemplate.hasKey(redisKey);
        if (Boolean.FALSE.equals(exists)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return false;
        }

        redisTemplate.expire(redisKey, TTL_EXTENSION_MIN, TimeUnit.MINUTES);


        return true;
    }
}
