package com.abdullayevtural.blog_post.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abdullayevtural.blog_post.dto.RegisterRequest;
import com.abdullayevtural.blog_post.entity.User;
import com.abdullayevtural.blog_post.repository.UserRepository;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public String register(RegisterRequest request) {
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new RuntimeException("Bu istifadeci adi artiq movcudddur!");

		}
		User user = new User();
		user.setUsername(request.getUsername());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		userRepository.save(user);

		return "Istifadeci ugurla qeydiyatdan kecdi!";

	}

}
