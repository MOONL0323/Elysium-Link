package com.example.demo.interceptor;



import com.example.demo.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.logging.Logger;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger logger = Logger.getLogger(JwtInterceptor.class.getName());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            //日志error
            logger.warning("token: " + token);
            token = token.substring(7);
            try {
                Long userId = JwtUtil.getUserIdFromToken(token);
                logger.warning("userId: " + userId);
                request.setAttribute("userId", userId);
            } catch (RuntimeException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return false;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }
        return true;
    }
}