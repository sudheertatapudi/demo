package com.playground.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.playground.demo.model.DemoUser;
import com.playground.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")  // Ensures only users with ADMIN role can access these endpoints
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<DemoUser>> getAllUsers() {
        List<DemoUser> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}