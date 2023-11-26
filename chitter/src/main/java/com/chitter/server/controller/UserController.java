package com.chitter.server.controller;

import com.chitter.server.model.User;
import com.chitter.server.repository.UserRepository;
import com.chitter.server.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins="*")
@RestController
@RequestMapping
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("signup")
    public ResponseEntity<Object> signUpUser(@RequestBody User newUser){

        try {
            if (userRepository.existsByEmail(newUser.getEmail())){
                return ResponseHandler.generateResponse("Email already linked to an account", HttpStatus.BAD_REQUEST, null);
            }

            if (userRepository.existsByUsername(newUser.getUsername())){
                return ResponseHandler.generateResponse("Username already linked to an account", HttpStatus.BAD_REQUEST, null);
            }

            userRepository.save(new User(newUser.getUsername(), newUser.getName(), newUser.getEmail(), newUser.getPassword()));

            return ResponseHandler.generateResponse("Registration successful", HttpStatus.CREATED, newUser);

        } catch (Exception e) {

            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {

        try {

            Optional<User> _user = userRepository.findByUsername(user.getUsername());

            if (_user.isPresent() && user.getPassword().equals(_user.get().getPassword())) {
                return ResponseHandler.generateResponse("User successfully logged in", HttpStatus.OK, _user);
            }

            return ResponseHandler.generateResponse("Unable to find login details", HttpStatus.BAD_REQUEST, null);

        } catch (Exception e) {

            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }
}
