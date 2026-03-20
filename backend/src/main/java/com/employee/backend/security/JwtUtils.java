package com.employee.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.*;
import org.springframework.stereotype.*;
import org.springframework.web.cors.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JwtUtils {

    //lấy ra khoá bí mặt trong application.properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    //lấy ra thời gian hết hạn token trong application.properties
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    //chuyển khoá thành dạng byte
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    //tạo ra token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)   //payload
                .setIssuedAt(new Date())    //thời điểm tạo
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) //thời điểm hết hạn
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)    //thêm phần khoá bí mật dạng byte đã được mã hoá theo hs256
                .compact(); //nối chúng lại
    }
    //lấy username từ token: giải mã token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()     //JWT parser (bộ đọc token): đọc token, verify chữ ký
                .setSigningKey(getSigningKey()).build()             //gắn cái khoá bí mật vào để lát kiểm tra bên dưới
                .parseClaimsJws(token).getBody().getSubject();      //parseClaimsJws(token): giải mã token, verity chữ ký, ktra hết hạn
    }                                                               //getBody lấy ra phần payload sau khi đc giải mã, getSubject lấy ra username
    //validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

