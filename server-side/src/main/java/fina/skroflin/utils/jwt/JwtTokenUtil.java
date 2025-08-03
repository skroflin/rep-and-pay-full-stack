/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author skroflin
 */
@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;
    private SecretKey signingKey;
    
    @PostConstruct
    public void init() {
        byte[] keyBytes = 
                Decoders.BASE64.decode(secret);
        this.signingKey = 
                Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(
            String token
    ){
        return extractClaim(
                token, claims -> (List<String>) 
                        claims.get("roles")
        );
    }
    
    public String extractTokenFromHeaders(HttpHeaders headers) {
        String authHeader = headers.getFirst("Authorization");
        if (authHeader == null 
                || !authHeader.startsWith("Bearer ")) {
            throw new 
        IllegalArgumentException("Authorization header is missing or malformed");
        }
        return authHeader.substring(7);
    }
    
    public <T> T extractClaim(
            String token, 
            Function<Claims, T> claimsResolver
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, Integer userId) 
    {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        claims.put("roles", roles);
        claims.put("UserId", userId);
        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(
            UserDetails userDetails, 
            Map<String, 
                    Object> extraClaims
    ) 
    {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        extraClaims.put("roles", roles);
        return createToken(extraClaims, userDetails.getUsername());
    }

    private String createToken(
            Map<String, Object> claims,
            String subject) 
    {
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }

    public Boolean validateToken(
            String token, 
            UserDetails userDetails
    ) 
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) 
                && !isTokenExpired(token));
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        try {
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}
