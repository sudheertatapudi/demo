package com.playground.demo.service;
import com.playground.demo.model.DemoUser;
import com.playground.demo.model.DemoRole;
import com.playground.demo.repository.UserRepository;
import com.playground.demo.repository.RoleRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor  @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public DemoUser saveUser(DemoUser user) {
        return userRepository.save(user);
    }

    @Override
    public DemoRole saveRole(DemoRole role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        DemoUser user = userRepository.findByUsername(username);
        DemoRole role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public DemoUser getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<DemoUser> getUsers() {
        return userRepository.findAll();
    }

      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DemoUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(DemoUser user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}