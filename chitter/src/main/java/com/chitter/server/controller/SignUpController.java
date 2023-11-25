package com.chitter.server.controller;

import com.chitter.server.model.User;
import com.chitter.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> signUpUser(@RequestBody User newUser){

        try {
            if (userRepository.existsByEmail(newUser.getEmail())){
                return new ResponseEntity<>("Email already linked to an account", HttpStatus.BAD_REQUEST);
            }
            if (userRepository.existsByUsername(newUser.getUsername())){
                return new ResponseEntity<>("Username already linked to an account", HttpStatus.BAD_REQUEST);
            }
            userRepository.save(new User(newUser.getUsername(), newUser.getName(), newUser.getEmail(), newUser.getPassword()));
            return new ResponseEntity<>("User successfully signed up", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}