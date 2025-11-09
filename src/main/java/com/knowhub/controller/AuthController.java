package com.knowhub.controller;

import com.knowhub.constant.AppConstants;
import com.knowhub.dto.ApiResponse;
import com.knowhub.dto.AuthRequest;
import com.knowhub.dto.AuthResponse;
import com.knowhub.model.User;
import com.knowhub.repository.UserRepository;
import com.knowhub.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller for user login and registration.
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@RestController
@RequestMapping(AppConstants.Api.AUTH)
@Tag(name = "Authentication", description = "User authentication and registration")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        
        User user = (User) authentication.getPrincipal();
        String token = jwtUtils.generateToken(user);
        
        return ResponseEntity.ok(ApiResponse.success(
            new AuthResponse(token, user.getUsername(), user.getRole().name())
        ));
    }
    
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Username already exists"));
        }
        
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.username() + "@example.com");
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(User.Role.USER);
        
        userRepository.save(user);
        
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", null));
    }
}