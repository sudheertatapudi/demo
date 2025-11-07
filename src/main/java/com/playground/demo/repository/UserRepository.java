package com.playground.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playground.demo.model.DemoUser;

public interface UserRepository extends JpaRepository<DemoUser, Long> {
    DemoUser findByUsername(String username);
}