package com.chitter.server.controller;

import com.chitter.server.model.ERole;
import com.chitter.server.model.Role;
import com.chitter.server.model.User;
import com.chitter.server.payload.req.LoginRequest;
import com.chitter.server.payload.req.SignUpRequest;
import com.chitter.server.payload.res.JwtResponse;
import com.chitter.server.repository.RoleRepository;
import com.chitter.server.repository.UserRepository;
import com.chitter.server.response.ResponseHandler;
import com.chitter.server.security.jwt.JwtUtils;
import com.chitter.server.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

//    @PostMapping("/signup")
//    public ResponseEntity<Object> signUpUser(@Valid @RequestBody SignUpRequest signUpRequest){
//
//        try {
//            if (userRepository.existsByEmail(signUpRequest.getEmail())){
//                return ResponseHandler.generateResponse("Email already linked to an account", HttpStatus.BAD_REQUEST, null);
//            }
//
//            if (userRepository.existsByUsername(signUpRequest.getUsername())){
//                return ResponseHandler.generateResponse("Username already linked to an account", HttpStatus.BAD_REQUEST, null);
//            }
//
//            userRepository.save(new User(signUpRequest.getUsername(), signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword()));
//
//            return ResponseHandler.generateResponse("Registration successful", HttpStatus.CREATED, newUser);
//
//        } catch (Exception e) {
//
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
//
//        }
//    }

    // add message response
    @PostMapping("/signup")
    public ResponseEntity<Object> signUpUser(@Valid @RequestBody SignUpRequest signUpRequest){

        try {
            if (userRepository.existsByEmail(signUpRequest.getEmail())){
                return ResponseHandler.generateResponse("Email already linked to an account", HttpStatus.BAD_REQUEST, null);
            }

            if (userRepository.existsByUsername(signUpRequest.getUsername())){
                return ResponseHandler.generateResponse("Username already linked to an account", HttpStatus.BAD_REQUEST, null);
            }

            User user = new User(signUpRequest.getUsername(), signUpRequest.getName(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
            Set<String> strRoles = signUpRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            if(strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: role not found"));
                roles.add(userRole);
            } else {
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: role not found"));
                roles.add(adminRole);
            }

            user.setRoles(roles);
            userRepository.save(user);

            return ResponseHandler.generateResponse("Registration successful", HttpStatus.CREATED, user);

        } catch (Exception e) {

            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<Object> loginUser(@RequestBody User user) {
//
//        try {
//
//            Optional<User> _user = userRepository.findByUsername(user.getUsername());
//
//            if (_user.isPresent() && user.getPassword().equals(_user.get().getPassword())) {
//                return ResponseHandler.generateResponse("User successfully logged in", HttpStatus.OK, _user);
//            }
//
//            return ResponseHandler.generateResponse("Unable to find login details", HttpStatus.BAD_REQUEST, null);
//
//        } catch (Exception e) {
//
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
//
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getName(), userDetails.getEmail(), roles));

        } catch (Exception e) {

            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }
}
