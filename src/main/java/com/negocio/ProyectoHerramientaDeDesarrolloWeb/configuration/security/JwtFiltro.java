package com.negocio.ProyectoHerramientaDeDesarrolloWeb.configuration.security;

import java.io.IOException;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Constructor para inyectar las dependencias
    public JwtFiltro(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = getToken(request);

        if (token != null) {
            System.out.println("Token recibido: " + token);

            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                System.out.println("Usuario extraído del token: " + username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    // Crea el contexto de seguridad
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Autenticación realizada con éxito.");
                }
            } else {
                System.out.println("Token no válido o expirado.");
            }
        } else {
            System.out.println("No se recibió token.");
        }

        filterChain.doFilter(request, response);
    }
    private String getToken(HttpServletRequest request) {
        final String authCabecera = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authCabecera) && authCabecera.startsWith("Bearer ")) {
            return authCabecera.substring(7);
        }
        return null;
    }


}
