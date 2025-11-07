package com.playground.demo.model;

import java.util.Collection;
import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class DemoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<DemoRole> roles = new ArrayList<>();

    public DemoUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void addRole(DemoRole role) {
        this.roles.add(role);
    }
}