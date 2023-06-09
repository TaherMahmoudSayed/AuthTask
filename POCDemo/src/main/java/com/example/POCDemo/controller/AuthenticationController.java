package com.example.POCDemo.controller;

import com.example.POCDemo.request.AuthenticationRequest;
import com.example.POCDemo.request.RegisterRequest;
import com.example.POCDemo.response.AuthenticationResponse;
import com.example.POCDemo.service.authService.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@PermitAll
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse>register(@RequestBody RegisterRequest registerRequest){
        AuthenticationResponse authResponse =authenticationService.register(registerRequest);
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse authResponse = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(authResponse);

    }
    @GetMapping("/refreshtoken")
    public void handelRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationResponse authResponse =
                authenticationService.handelRefreshToken( request, response);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),authResponse);

    }
}
