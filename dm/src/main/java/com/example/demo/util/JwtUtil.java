package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

public class JwtUtil {
    private static final String SECRET_KEY = "access_token_secret";
    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());
    private static final long EXPIRATION_TIME = 86; // 1 天的毫秒数

    public static String generateToken(String username, Long userId) throws UnsupportedEncodingException {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes("UTF-8"))
                .compact();
    }

    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setAllowedClockSkewSeconds(999999999)// 允许一天的时钟偏差
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
        } catch (SignatureException e) {
            logger.severe("Invalid token: " + e.getMessage());
            throw new RuntimeException("Invalid token", e);
        } catch (Exception e) {
            logger.severe("Token parsing error: " + e.getMessage());
            throw new RuntimeException("Token parsing error", e);
        }
    }

    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        logger.info("Claims: " + claims);
        Object id = claims.get("id");
        if (id == null) {
            throw new RuntimeException("Token does not contain user id");
        }
        return Long.parseLong(id.toString());
    }
}