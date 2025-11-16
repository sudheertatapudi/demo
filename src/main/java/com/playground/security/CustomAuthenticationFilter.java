package com.playground.security;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // override attemptAuthentication to customize authentication process
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Custom logic here (e.g., logging, additional checks)
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    log.info("Attempting authentication for user: {} with password : {}", username, password); 

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

               
                UserDetails userDetails = (UserDetails) authResult.getPrincipal();

                log.info("User {} authenticated successfully with authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());

                Algorithm algorithm = Algorithm.HMAC256("secretkey".getBytes());

        
               String access_token = com.auth0.jwt.JWT.create()
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).toList())
                    .sign(algorithm);


                 String refresh_token = com.auth0.jwt.JWT.create()
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 30*60*1000))
                    .withIssuer(request.getRequestURL().toString())
                    .sign(algorithm);    


                    response.setHeader("access_token", access_token);
                    response.setHeader("refresh_token", refresh_token);

                    Map<String, String> tokens = Map.of(
                        "access_token", access_token,
                        "refresh_token", refresh_token
                    );
                    response.setContentType("application/json");
                    new com.fasterxml.jackson.databind.ObjectMapper().writeValue(response.getOutputStream(), tokens);
                    

              //  super.successfulAuthentication(request, response, chain, authResult);
    }


}
