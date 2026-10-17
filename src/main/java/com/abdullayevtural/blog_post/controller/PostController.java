package com.abdullayevtural.blog_post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.blog_post.dto.PostRequest;
import com.abdullayevtural.blog_post.dto.PostResponse;
import com.abdullayevtural.blog_post.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping
	public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request, Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(postService.createPost(request, username));

	}

	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPosts() {
		return ResponseEntity.ok(postService.getAllPost());

	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
		return ResponseEntity.ok(postService.getPostById(id));

	}

	@PutMapping("/{id}")
	public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest request,
			Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(postService.updatePost(id, request, username));

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable Long id, Authentication authentication) {
		String username = authentication.getName();
		postService.deletePost(id, username);
		return ResponseEntity.ok("Post ugurla silindi!");

	}

}
