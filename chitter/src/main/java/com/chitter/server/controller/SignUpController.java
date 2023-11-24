package com.chitter.server.controller;

import com.chitter.server.model.User;
import com.chitter.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> signUpUser(@RequestBody User newUser){

        try {
            userRepository.save(new User(newUser.getUsername(), newUser.getName(), newUser.getEmail(), newUser.getPassword()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
