package com.example.marshAssignment.controller;

import com.example.marshAssignment.exception.ApiErrorResponse;
import com.example.marshAssignment.model.AuthenticationRequest;
import com.example.marshAssignment.model.AuthenticationResponse;
import com.example.marshAssignment.service.UserDetailService;
import com.example.marshAssignment.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static java.util.Arrays.asList;

@RestController
@CrossOrigin()
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserDetailService userDetailService;
    private JWTUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserDetailService userDetailService, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @Valid @RequestBody AuthenticationRequest authenticationRequest
    ) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUserName(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password");
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUserName());
        return ResponseEntity.ok(new AuthenticationResponse(jwtUtil.generateToken(userDetails)));
    }

    @GetMapping("/test")
    public String test() {
        return "test Successful";
    }

    // Controller Specific exception handler
    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> responseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ApiErrorResponse(e.getReason(), asList("other detail")));
    }
}
