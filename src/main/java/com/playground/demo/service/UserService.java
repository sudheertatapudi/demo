package com.playground.demo.service;
import com.playground.demo.model.DemoUser;
import com.playground.demo.model.DemoRole;
import java.util.List;

public interface UserService {
    // Define user-related service methods here

    DemoUser saveUser(DemoUser user);
    DemoRole saveRole(DemoRole role);
    void addRoleToUser(String username, String roleName);
    DemoUser getUser(String username);
    List<DemoUser> getUsers();
}