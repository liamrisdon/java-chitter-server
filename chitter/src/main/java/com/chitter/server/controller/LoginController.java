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

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> loginUser(@RequestBody User user) {

        try {
            Optional<User> _user = userRepository.findByUsername(user.getUsername());

            if (_user.isPresent() && user.getPassword().equals(_user.get().getPassword())) {
                return new ResponseEntity<>("User successfully logged in", HttpStatus.OK);
            }
            return new ResponseEntity<>("Unable to find login details", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
