package com.edu.harran.social.service;


import com.edu.harran.social.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET_KEY = "9208cf9t2f2a8f01fl19af7g8d35560d1zd8feca7fb43514cde2023e782b8c2a";

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public String extractEmail(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public boolean isValid(String token, UserDetails user) {
        User newUser = (User) user;
        String email = extractEmail(token);
        return (email.equals(newUser.getEmail())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        try{
            Claims claims = extractAllClaims(token);
            return resolver.apply(claims);
        }catch (ExpiredJwtException e){
            return null;
        }
    }
    private Claims extractAllClaims(String token)  {
            return Jwts.parser()
                    .verifyWith(getSigninKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
    //24*60*60*1000
    public String generateToken(User user){
        String token = Jwts.builder().
                subject(user.getEmail()).
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis()+24*10*60*60*1000))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    public String generateRefreshToken(User user){
        String refreshToken = Jwts.builder().
                subject(user.getEmail()).
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis()+7*24*60*60*1000))
                .signWith(getSigninKey())
                .compact();
        return refreshToken;
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
