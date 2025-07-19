package org.sebas.blogbackendspringboot.controller;

import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public User register(@RequestBody User user){

        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        System.out.println(user);

        return userService.verify(user);
    }
}
