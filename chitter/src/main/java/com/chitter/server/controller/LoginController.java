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
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserRepository userRepository;

    //    @PostMapping
//    public ResponseEntity<Object> loginUser(@RequestBody User user) {
//
//        try {
//            Optional<User> _user = userRepository.findByUsername(user.getUsername());
//
//            if (_user.isPresent() && user.getPassword().equals(_user.get().getPassword())) {
//                return new ResponseEntity<>("User successfully logged in", HttpStatus.OK);
//            }
//            return new ResponseEntity<>("Unable to find login details", HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    @PostMapping
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
