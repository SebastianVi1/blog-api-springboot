package org.sebas.blogbackendspringboot.service;

import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Register a new user
     * Encrypts password before saving to database
     */
    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return user;
    }

    public List<User> usersList() {
        return repo.findAll();
    }

    /**
     * Verify user credentials and generate JWT token
     * Authenticates user and returns JWT token if successful
     */
    public String verify(String username, String password) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(username);
        return "fail";
    }
}