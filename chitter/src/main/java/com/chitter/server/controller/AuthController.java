package com.chitter.server.controller;

import com.chitter.server.model.ERole;
import com.chitter.server.model.Role;
import com.chitter.server.model.User;
import com.chitter.server.payload.req.LoginRequest;
import com.chitter.server.payload.req.SignUpRequest;
import com.chitter.server.payload.res.JwtResponse;
import com.chitter.server.payload.res.MessageResponse;
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

    @PostMapping("/signup")
    public ResponseEntity<Object> signUpUser(@Valid @RequestBody SignUpRequest signUpRequest){

        try {
            if (userRepository.existsByEmail(signUpRequest.getEmail())){
                return ResponseEntity.badRequest().body(new MessageResponse("Email already linked to an account"));
            }

            if (userRepository.existsByUsername(signUpRequest.getUsername())){
                return ResponseEntity.badRequest().body(new MessageResponse("Username already linked to an account"));
            }

            User user = new User(signUpRequest.getUsername(), signUpRequest.getName(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
            Set<String> strRoles = signUpRequest.getRoles();
            Set<Role> roles = new HashSet<>();


            if (strRoles == null || strRoles.isEmpty()) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    Role roleToAdd;
                    switch (role) {
                        case "admin":
                            roleToAdd = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            break;
                        default:
                            roleToAdd = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    }
                    roles.add(roleToAdd);
                });
            }

                user.setRoles(roles);
                userRepository.save(user);

                return ResponseEntity.ok(new MessageResponse("Registration successful"));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));

        }
    }

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

            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));

        }
    }
}
