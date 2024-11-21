package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.util.logging.Logger;

public class JwtUtil {
    private static final String SECRET_KEY = "access_token_secret";
    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes("UTF-8"))
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