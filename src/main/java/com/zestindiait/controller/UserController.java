package com.zestindiait.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zestindiait.dto.JwtResponse;
import com.zestindiait.dto.LoginRequest;
import com.zestindiait.entity.User;
import com.zestindiait.security.JwtHelper;
import com.zestindiait.service.UserService;

@RestController
@RequestMapping("/api/public")  
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.registerUser(user) != null) {
            return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/login-user")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        User user = userService.loginUser(request);
        if (user != null) {
            String token = jwtHelper.generateToken(user.getUsername());
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
}
