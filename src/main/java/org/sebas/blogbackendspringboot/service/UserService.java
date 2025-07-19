package org.sebas.blogbackendspringboot.service;

import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    public User register(User user){
        repo.save(user);
        return user;
    }

    public List<User> usersList() {
        return repo.findAll();
    }

    public String verify(User user) {
        Authentication authentication =
                authManager.authenticate( new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "fail";
    }
}