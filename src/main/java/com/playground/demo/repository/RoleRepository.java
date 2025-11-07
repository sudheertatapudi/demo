package com.playground.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playground.demo.model.DemoRole;

public interface RoleRepository extends JpaRepository<DemoRole, Long> {
    DemoRole findByName(String name);
}