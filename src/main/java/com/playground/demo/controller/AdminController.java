package com.playground.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Stream;

import javax.management.relation.Role;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.playground.demo.model.DemoUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.playground.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static java.util.Arrays.stream;
import com.playground.demo.model.DemoRole;


@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")  // Ensures only users with ADMIN role can access these endpoints
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService  userService;

    @GetMapping("/users")
    public ResponseEntity<List<DemoUser>> getAllUsers() {
        List<DemoUser> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

   // ADD USEER
    @PostMapping("/user/save")
    public ResponseEntity<DemoUser> saveUser(@RequestBody DemoUser user) {
        DemoUser savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // add role
    @PostMapping("/role/save")
    public ResponseEntity<?> saveRole(@RequestBody com.playground.demo.model.DemoRole role) {
        com.playground.demo.model.DemoRole savedRole = userService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    // add role to user
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }   

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
         String authorizationHeader = request.getHeader("Authorization");
             if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

                try{

                    String refresh_token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secretkey".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT jwt = verifier.verify(refresh_token);
                    String username = jwt.getSubject();
                      
                    DemoUser userDetails = userService.getUser(username);

                     String access_token = com.auth0.jwt.JWT.create()
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", userDetails.getRoles().stream().map(DemoRole::getName).toList())
                    .sign(algorithm);


               
                   Map<String, String> tokens = Map.of(
                        "access_token", access_token,
                        "refresh_token", refresh_token
                    );
                    response.setContentType("application/json");
                    new com.fasterxml.jackson.databind.ObjectMapper().writeValue(response.getOutputStream(), tokens);

                          


                }
                catch(Exception e){

                    log.error("Error logging in: {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    
                   
                }




             }
                else {
                    throw new RuntimeException("Refresh token is missing");
                }

    }

   
}

 // DTO class for role to user form
    @Data 
    class RoleToUserForm {
        private String username;
        private String roleName;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }