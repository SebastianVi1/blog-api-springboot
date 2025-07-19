package org.sebas.blogbackendspringboot.controller;

import jakarta.validation.Valid;
import org.sebas.blogbackendspringboot.dto.UserLoginDto;
import org.sebas.blogbackendspringboot.dto.UserRegistrationDto;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     * Validates user input using UserRegistrationDto
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto registrationDto){
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Login user and return JWT token
     * Validates login credentials using UserLoginDto
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto loginDto){
        String token = userService.verify(loginDto.getUsername(), loginDto.getPassword());
        return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
    }
}
