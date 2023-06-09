package com.example.POCDemo.Configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
//extracting claims and checking if a token is valid
public class JwtService {
    private static final String SECRET_KEY = "6D5A7134743777217A25432A46294A404E635266556A586E3272357538782F41";

    // 1 first we extract all claims
    private Claims extractJwtClaims(String jwt) {

        return Jwts.
                parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String jwt) {
        String username = extractClaim(jwt, Claims::getSubject);
        return username;
    }

    // 2 second we apply claimssolver function to extract the needed claim ex username / expiration..ect .
    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = extractJwtClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateJwt(Map<String, List<String>> claims, UserDetails userDetails) {
        try {
            String token = Jwts
                    .builder()
                    .setClaims(claims)// here in claims you can add whatever claims or credentials you want.{"Role":["user","manager"]}
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 1)))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }

    public String generateRefreshJWT(Map<String, List<String>> claims, UserDetails userDetails) {
        try {
            String token = Jwts
                    .builder()
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5)))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public boolean isJwtValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isJwtExpired(jwt);
    }

    private boolean isJwtExpired(String jwt) {
        //to make sure that the time now is less than (time yesterday+1 day after )
        return extractJwtExpiration(jwt).before(new Date());
    }

    private Date extractJwtExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }


}
