package com.playground.demo.config;

import com.playground.demo.model.DemoRole;
import com.playground.demo.model.DemoUser;
import com.playground.demo.repository.RoleRepository;
import com.playground.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        DemoRole roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = new DemoRole("ROLE_USER");
            roleRepository.save(roleUser);
            log.info("Created ROLE_USER");
        }

        DemoRole roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new DemoRole("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
            log.info("Created ROLE_ADMIN");
        }

        roleRepository.save(new DemoRole("ROLE_MANAGER"));
        roleRepository.save(new DemoRole("ROLE_SUPER_ADMIN"));

        // Create test users if they don't exist
        if (userRepository.findByUsername("user") == null) {
            DemoUser user = new DemoUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setEmail("user@example.com");
            user.addRole(roleUser);
            userRepository.save(user);
            log.info("Created test user: username=user, password=password");
        }

        if (userRepository.findByUsername("admin") == null) {
            DemoUser admin = new DemoUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.addRole(roleUser);
            admin.addRole(roleAdmin);
            userRepository.save(admin);
            log.info("Created admin user: username=admin, password=admin");
        }

        if (userRepository.findByUsername("sudheer") == null) {
            DemoUser sudheer = new DemoUser();
            sudheer.setUsername("sudheer");
            sudheer.setPassword(passwordEncoder.encode("sudheer"));
            sudheer.setEmail("sudheer@example.com");
            sudheer.addRole(roleUser);
            sudheer.addRole(roleAdmin);
            userRepository.save(sudheer);
            log.info("Created admin user: username=sudheer, password=sudheer");
        }

         if (userRepository.findByUsername("manju") == null) {
            DemoUser manju = new DemoUser();
            manju.setUsername("manju");
            manju.setPassword(passwordEncoder.encode("manju"));
            manju.setEmail("manju@example.com");
            manju.addRole(roleUser);
            userRepository.save(manju);
            log.info("Created regular user: username=manju, password=manju");
        }

        log.info("Database initialization completed");
    }
}
