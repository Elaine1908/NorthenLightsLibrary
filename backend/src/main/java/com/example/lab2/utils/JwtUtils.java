package com.example.lab2.utils;

import com.example.lab2.entity.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class JwtUtils {

    private static final String secret = UUID.randomUUID().toString();//jwt签名时的盐值
    private static final long shelfLife = (long) 60 * 60 * 1000;//每个签发的jwt有效期是1小时
    private static final SignatureAlgorithm algo = SignatureAlgorithm.HS256;//jwt签名算法


    /**
     * 根据用户生成jwt token串
     *
     * @param user 用户
     * @return 代表用户的jwt串
     */
    public static String generateJwt(User user) {
        return Jwts.builder()
                .signWith(algo, secret)
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .claim("libraryID", user.getLibraryID())
                .setExpiration(new Date(System.currentTimeMillis() + shelfLife))
                .compact();

    }

    /**
     * 根据jwt串，把用户名读出来
     *
     * @param token jwt
     * @return 用户名
     */
    public static String getUserName(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims.get("username", String.class);
        } catch (IllegalArgumentException | SignatureException | ExpiredJwtException e) {
            return null;
        }
    }

    /**
     * 根据jwtToken中的libraryID，获得管理员的上班地点
     *
     * @param token token
     * @return
     */
    public static Long getLibraryID(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims.get("libraryID", Long.class);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据token，返回用户的权限列表
     *
     * @param token token
     * @return 用户的权限列表
     */
    public static List<SimpleGrantedAuthority> getRole(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            String role = claims.get("role", String.class);
            ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role));
            return simpleGrantedAuthorities;
        } catch (IllegalArgumentException | SignatureException | ExpiredJwtException e) {
            return null;
        }
    }


    /**
     * 根据jwttoken，解析用户是否是管理员，还是普通的用户
     *
     * @param token 前端传来的token
     * @return 用户是否是管理员
     */
    public static boolean getIsAdmin(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            String role = claims.get("role", String.class);
            return role.equals(User.ADMIN) || role.equals(User.SUPERADMIN);
        } catch (IllegalArgumentException | SignatureException | ExpiredJwtException e) {
            return false;
        }
    }


}
