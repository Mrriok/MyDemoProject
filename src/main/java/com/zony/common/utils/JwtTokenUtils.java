package com.zony.common.utils;

import cn.hutool.core.text.StrSpliter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

/**
 * @Description: JWT token工具类
 * @Date 2020/2/29 15:43
 * TOKEN_HEADER 用于获取前端request中的token
 * TOKEN_PREFIX 用户标记token源
 * SECRET ISS 生成token用的秘钥
 * ROLE_CLAIMS 生成token用的角色信息
 * EXPIRATION 设置token过期时间
 * EXPIRATION_REMEMBER 基于RememberMe如为true则token有效时间为7天可灵活配置，false则基于默认设置的过期时间
 * @Author ZLK
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "WM_";
    public static final String TOKEN_PREFIXSSO = "SSO_";

    private static final String SECRET = "dpms";
    private static final String ISS = "scp";

    // 角色的key
    private static final String ROLE_CLAIMS = "role";
    private static final String USERINFO_CLAIMS = "userinfo";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 28800L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;

    // 创建token
    public static String createToken(String userId, String role, String userInfo, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        map.put(USERINFO_CLAIMS, userInfo);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    // 从token中获取用户名
    public static String getUserId(String token){
        return getTokenBody(token).getSubject();
    }

    // 从token中获取用户信息
    public static String getUserInfo(String token){
        return (String) getTokenBody(token).get(USERINFO_CLAIMS);
    }

    // 获取用户角色
    public static String getUserRole(String token){
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    // 是否已过期
    public static boolean isExpiration(String token) {
        try {
            return getTokenBody(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private static Claims getTokenBody(String token){
        token = tokenSplit(token);
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
    public static String  tokenSplit(String token){
        if(token.contains(TOKEN_PREFIX)){
            token = StrSpliter.split(token,TOKEN_PREFIX,0,true,true).get(0);
        }else if(token.contains(TOKEN_PREFIXSSO)){
            token = StrSpliter.split(token,TOKEN_PREFIXSSO,0,true,true).get(0);
        }
        return token;
    }

    public static String  getPrefix(String token){
        String prefix="";
        if(token.contains(TOKEN_PREFIX)){
            prefix =  TOKEN_PREFIX;
        }else if(token.contains(TOKEN_PREFIXSSO)){
            prefix =  TOKEN_PREFIXSSO;
        }
        return prefix;
    }
}
