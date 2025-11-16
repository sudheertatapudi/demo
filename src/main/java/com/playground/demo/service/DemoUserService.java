package com.playground.demo.service;

import com.playground.demo.model.DemoUser;
import com.playground.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DemoUserService {

    private final UserRepository userRepository;

    public List<DemoUser> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<DemoUser> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public DemoUser findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public DemoUser saveUser(DemoUser user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findAll().stream()
            .anyMatch(user -> user.getEmail().equals(email));
    }

    @Transactional
    public DemoUser updateUser(Long id, DemoUser userDetails) {
        DemoUser existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        // Note: Password update should be handled separately with proper encryption
        existingUser.setRoles(userDetails.getRoles());

        return userRepository.save(existingUser);
    }
}