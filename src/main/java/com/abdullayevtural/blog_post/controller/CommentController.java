package com.abdullayevtural.blog_post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.blog_post.dto.CommentRequest;
import com.abdullayevtural.blog_post.dto.CommentResponse;
import com.abdullayevtural.blog_post.service.CommentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/posts")
public class CommentController {
	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/{postId}/comments")
	public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId, @RequestBody CommentRequest request,
			Authentication authentication) {
		String username = authentication.getName();
		CommentResponse response = commentService.addComment(postId, request, username);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{postId}/comments")
	public ResponseEntity<Page<CommentResponse>> getCommentsByPostId(@PathVariable Long postId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "DESC") String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<CommentResponse> comments = commentService.getCommentsByPostId(postId, pageable);
		return ResponseEntity.ok(comments);
	}

}
