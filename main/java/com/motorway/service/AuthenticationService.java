package com.motorway.service;

import com.motorway.enums.UserRole;
import com.motorway.model.User;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private static AuthenticationService instance;
    private final Map<String, User> users = new HashMap<>();
    private User currentUser;

    private AuthenticationService() {
        initializeDefaultUsers();
    }

    public static synchronized AuthenticationService getInstance() {
        if (instance == null) instance = new AuthenticationService();
        return instance;
    }

    private void initializeDefaultUsers() {
        users.put("admin", new User(1, "admin", "admin123", "System Administrator", UserRole.ADMIN));
        users.put("user", new User(2, "user", "user123", "Regular User", UserRole.USER));
    }

    public User login(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(password) && u.isActive()) {
            currentUser = u;
            System.out.println("User logged in: " + u.getFullName());
            return u;
        }
        return null;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("User logged out: " + currentUser.getFullName());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}