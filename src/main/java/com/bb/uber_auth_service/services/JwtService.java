package com.bb.uber_auth_service.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    public String createToken(Map<String, Object> payload, String email){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry*1000L);
        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .subject(email)
                .signWith(getSigningKey())
                .compact();
    }

    public String createToken(String email){
        return createToken(new HashMap<>(), email);
    }

    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /***
     * Validates the JWT token by verifying its signature and checking its expiration.
     * @param token to be validated
     * @param email to check against the token's claims.
     * @return
     */
    public Boolean validateToken(String token, String email){
        final String userEmailFetchedFromToken = extractEmail(token);
        return (userEmailFetchedFromToken.equals(email)) && !isTokenExpired(token);
    }

    public Claims extractPayload(String token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractPayload(token);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


}
