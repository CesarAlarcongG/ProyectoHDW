package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Clave constante

    // Generamos tokens
    public String getToken(UserDetails usuario) {
        return getToken(new HashMap<>(), usuario);
    }

    public String getToken(Map<String,Object> extraClaim, UserDetails usuario){
        return Jwts
                .builder()
                .setClaims(extraClaim)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 10000 * 60 * 24)) // 24 horas
                .signWith(key, SignatureAlgorithm.HS256) // Usa la clave constante
                .compact();
    }

    // Validación de tokens
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // Usa la misma clave
            System.out.println("Se valido correcta mente -------------------------------------------------Clase:JwtService");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Hay un problema con la validación ----------------------------------------Clase:JwtService");
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
