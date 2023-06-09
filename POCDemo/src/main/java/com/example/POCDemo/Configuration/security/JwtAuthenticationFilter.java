package com.example.POCDemo.Configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal
            (HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //checking permitted paths
            if (request.getServletPath().equals("/api/v1/auth/register")
                || request.getServletPath().equals("/api/v1/auth/authenticate")
                ||request.getServletPath().equals("/api/v1/auth/refreshtoken")) {
                filterChain.doFilter(request, response);
                return;
            }
                final String authHeader = request.getHeader("Authorization");
                final String jwt;
                final String username;
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                jwt = authHeader.substring(7);
                username = jwtService.extractUsername(jwt);// to do implement this method
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (userDetails != null) {
                        if (jwtService.isJwtValid(jwt, userDetails)) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                            authToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);

                        }
                    }
                }else {
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.sendError(FORBIDDEN.value(), "unauthorized user ");
                }



            }catch(Exception ex)
            {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.sendError(FORBIDDEN.value(), ex.getMessage());
                //you can here send a custom response
            }finally {
            filterChain.doFilter(request, response);
        }



    }
}
