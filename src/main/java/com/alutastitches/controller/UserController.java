package com.alutastitches.controller;

import com.alutastitches.dto.UserDto;
import com.alutastitches.model.Users;
import com.alutastitches.serviceImpli.UserServiceImpl;
import com.alutastitches.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

@Autowired
    public UserController(@Lazy UserServiceImpl userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this. jwtUtils = jwtUtils;
    }
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto){
    Users user = userService.saveUser(userDto);
    UserDto userDto1 = new ObjectMapper().convertValue(user, UserDto.class);
    return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto){
        UserDetails user = userService.loadUserByUsername(userDto.getUsername());
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            String jwtToken = jwtUtils.createJwt.apply(user);
            return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("username and password incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(){
    return "Welcome to your Dashboard";
    }
}
