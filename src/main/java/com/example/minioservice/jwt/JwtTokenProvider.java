package com.example.minioservice.jwt;

import com.example.minioservice.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims;
        try {
             claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        }catch (Exception e){
            throw new InvalidTokenException("The token is not correct!");
        }
        return !claims.getBody().getExpiration().before(new Date());
    }

    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private List<SimpleGrantedAuthority> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        LinkedHashMap rolesMap = (LinkedHashMap) claims.get("roles");

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> roles = rolesMap.values().stream().toList();
        authorities.add(new SimpleGrantedAuthority(roles.get(1)));
        return authorities;
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        List<SimpleGrantedAuthority> roles = getRoles(token);

        User principal = new User(username, "", roles);

        return new UsernamePasswordAuthenticationToken(principal, token, roles);
    }
}