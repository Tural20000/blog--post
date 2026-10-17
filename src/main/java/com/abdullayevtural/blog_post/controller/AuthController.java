package com.abdullayevtural.blog_post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.blog_post.dto.AuthResponse;
import com.abdullayevtural.blog_post.dto.LoginRequest;
import com.abdullayevtural.blog_post.dto.RegisterRequest;
import com.abdullayevtural.blog_post.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
		String responseMessage = authService.register(request);
		return ResponseEntity.ok(responseMessage);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

}
