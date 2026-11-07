package com.abdullayevtural.blog_post.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abdullayevtural.blog_post.dto.PostReportResponse;
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

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostResponse> createPost(@RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam(value = "file", required = false) MultipartFile file,
			Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(postService.createPostWithImage(title, content, file, username));
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

	@GetMapping("/reports")
	public ResponseEntity<List<PostReportResponse>> getPostReports(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			Authentication authentication) {
		String username = authentication.getName();
		List<PostReportResponse> reports = postService.getPostReportsByDateRange(username, startDate, endDate);
		return ResponseEntity.ok(reports);
	}
}