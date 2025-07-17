package com.cms.security;

import java.security.Key;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

@Component //tells Spring to detect this class automatically for use (so you can inject it into other classes later)
public class JwtUtil {
    private final String SECRET_KEY = "this_is_a_super_key_for_jwt_256_bit_long";

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in milliseconds

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    };//Converts your secret key into a proper Key object for signing JWTs using the jjwt library.

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)   //who the token is about
                .setIssuedAt(new Date())    // when it was created
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))    // expires after 1 hour
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)    // sign it with secret key
                .compact(); // make it a JWT string
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // pull the username from inside the token
    }   // Reads the token, verifies it with the secret key, and pulls out the username (subject).


    public boolean isTokenValid(String token, String username){
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }   // Checks the username in the token matches and token is not expired

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }   // Extracts the expiration time from the token and checks if it's expired
}
